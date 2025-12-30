package net.jensensagastudio.continuumuniverses.world.teleporter;

import net.jensensagastudio.continuumuniverses.block.ModBlocks;
import net.jensensagastudio.continuumuniverses.world.poi.ModPOIs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.BlockUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Comparator;
import java.util.Optional;

public class UvlaTeleporter {

	private static final int SEARCH_RADIUS_SPAWN = 16;
	private static final int SEARCH_RADIUS_ANY   = 128;

	private final ServerLevel level;

	public UvlaTeleporter(ServerLevel level) {
		this.level = level;
	}

	public Optional<BlockPos> findClosestPortalPosition(BlockPos origin, boolean isNearSpawn, WorldBorder border) {
		PoiManager poiManager = level.getPoiManager();
		int radius = isNearSpawn ? SEARCH_RADIUS_SPAWN : SEARCH_RADIUS_ANY;

		// Make sure POIs are loaded in the search square
		poiManager.ensureLoadedAndValid(level, origin, radius);

		// If the POI holder isn't ready for some reason, fail gracefully instead of exploding.
		if (ModPOIs.UVLA_PORTAL_POI == null || ModPOIs.UVLA_PORTAL_POI.unwrapKey().isEmpty()) {
			return Optional.empty();
		}

		return poiManager
				.getInSquare(
						poi -> poi.is(ModPOIs.UVLA_PORTAL_POI.unwrapKey().get()),
						origin,
						radius,
						PoiManager.Occupancy.ANY
				)
				.map(PoiRecord::getPos)
				.filter(border::isWithinBounds)
				.filter(pos -> level.getBlockState(pos).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
				.min(Comparator.<BlockPos>comparingDouble(pos -> pos.distSqr(origin)).thenComparingInt(Vec3i::getY));
	}

	public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos origin, Direction.Axis axis) {
		Direction forward = Direction.get(Direction.AxisDirection.POSITIVE, axis);
		WorldBorder worldBorder = level.getWorldBorder();

		int maxY = Math.min(
				level.getMaxY(),
				level.getMinY() + level.getLogicalHeight() - 1
		);

		BlockPos.MutableBlockPos scratch = origin.mutable();

		BlockPos bestPos = null;
		double bestDist = -1.0;

		BlockPos backupPos = null;
		double backupDist = -1.0;

		// Search spiral around origin, similar to vanilla behavior
		for (BlockPos.MutableBlockPos cursor : BlockPos.spiralAround(origin, 16, Direction.EAST, Direction.SOUTH)) {
			int topY = Math.min(maxY, level.getHeight(Heightmap.Types.MOTION_BLOCKING, cursor.getX(), cursor.getZ()));

			if (!worldBorder.isWithinBounds(cursor) || !worldBorder.isWithinBounds(cursor.move(forward, 1))) {
				continue;
			}

			cursor.move(forward.getOpposite(), 1);

			for (int y = topY; y >= level.getMinY(); y--) {
				cursor.setY(y);

				if (!canPortalReplaceBlock(cursor)) continue;

				int startY = y;
				while (y > level.getMinY() && canPortalReplaceBlock(cursor.move(Direction.DOWN))) {
					y--;
				}

				if (y + 4 > maxY) continue;

				int depth = startY - y;
				if (depth <= 0 || depth >= 3) continue;

				cursor.setY(y);

				if (!canHostFrame(cursor, scratch, forward, 0)) continue;

				double dist = origin.distSqr(cursor);
				boolean hasLeft  = canHostFrame(cursor, scratch, forward, -1);
				boolean hasRight = canHostFrame(cursor, scratch, forward, 1);

				if (hasLeft && hasRight && (bestDist == -1.0 || dist < bestDist)) {
					bestDist = dist;
					bestPos = cursor.immutable();
				} else if (bestDist == -1.0 && (backupDist == -1.0 || dist < backupDist)) {
					backupDist = dist;
					backupPos = cursor.immutable();
				}
			}
		}

		if (bestDist == -1.0 && backupDist != -1.0) {
			bestPos = backupPos;
			bestDist = backupDist;
		}

		// If no valid spot, place a portal platform in a reasonable Y-range
		if (bestDist == -1.0) {
			int minY = Math.max(level.getMinY() + 1, 70);
			int maxPlaceY = maxY - 9;
			if (maxPlaceY < minY) return Optional.empty();

			bestPos = new BlockPos(
					origin.getX() - forward.getStepX(),
					Mth.clamp(origin.getY(), minY, maxPlaceY),
					origin.getZ() - forward.getStepZ()
			);

			bestPos = worldBorder.clampToBounds(bestPos);

			Direction right = forward.getClockWise();

			// Clear and build a small base
			for (int dx = -1; dx < 2; dx++) {
				for (int dz = 0; dz < 2; dz++) {
					for (int dy = -1; dy < 3; dy++) {
						BlockState fill = (dy < 0)
								? ModBlocks.LETHURKEST_BLOCK.get().defaultBlockState()
								: Blocks.AIR.defaultBlockState();

						scratch.setWithOffset(
								bestPos,
								dz * forward.getStepX() + dx * right.getStepX(),
								dy,
								dz * forward.getStepZ() + dx * right.getStepZ()
						);

						level.setBlockAndUpdate(scratch, fill);
					}
				}
			}
		}

		// Build frame
		for (int fx = -1; fx < 3; fx++) {
			for (int fy = -1; fy < 4; fy++) {
				if (fx == -1 || fx == 2 || fy == -1 || fy == 3) {
					scratch.setWithOffset(bestPos, fx * forward.getStepX(), fy, fx * forward.getStepZ());
					level.setBlock(scratch, ModBlocks.LETHURKEST_BLOCK.get().defaultBlockState(), 3);
				}
			}
		}

		// Fill portal and add POI entries
		BlockState portalState = ModBlocks.UVLA_PORTAL.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);

		for (int px = 0; px < 2; px++) {
			for (int py = 0; py < 3; py++) {
				scratch.setWithOffset(bestPos, px * forward.getStepX(), py, px * forward.getStepZ());
				level.setBlock(scratch, portalState, 18);

				if (ModPOIs.UVLA_PORTAL_POI != null) {
					level.getPoiManager().add(scratch, ModPOIs.UVLA_PORTAL_POI);
				}
			}
		}

		return Optional.of(new BlockUtil.FoundRectangle(bestPos, 2, 3));
	}

	private boolean canHostFrame(BlockPos origin, BlockPos.MutableBlockPos scratch, Direction forward, int sidewaysOffset) {
		Direction right = forward.getClockWise();

		for (int x = -1; x < 3; x++) {
			for (int y = -1; y < 4; y++) {
				scratch.setWithOffset(
						origin,
						forward.getStepX() * x + right.getStepX() * sidewaysOffset,
						y,
						forward.getStepZ() * x + right.getStepZ() * sidewaysOffset
				);

				if (y < 0 && !level.getBlockState(scratch).isSolid()) return false;
				if (y >= 0 && !canPortalReplaceBlock(scratch)) return false;
			}
		}

		return true;
	}

	private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return state.canBeReplaced() && state.getFluidState().isEmpty();
	}
}
