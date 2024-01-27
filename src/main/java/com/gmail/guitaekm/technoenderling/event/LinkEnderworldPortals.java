package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.items.ModItems;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LinkEnderworldPortals implements OnStructureActivate.Listener {
    final static private LinkEnderworldPortals instance = new LinkEnderworldPortals();
    public static LinkEnderworldPortals getInstance() {
        return LinkEnderworldPortals.instance;
    }
    public void registerClass() {
        OnStructureActivate.getInstance().register(this);
    }
    @Override
    public ActionResult onStructureActivate(ServerPlayerEntity player, ServerWorld world, EnderlingStructure portal, BlockPos root) {
        if (!portal.getId().equals(new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal"))) {
            return ActionResult.PASS;
        }
        ServerWorld otherWorld, enderworld;
        DimensionFinder finder = new DimensionFinder(new Identifier(TechnoEnderling.MOD_ID, "enderworld"));
        finder.lazyInit(world.getServer());
        enderworld = world.getServer().getWorld(
                finder.get()
        );
        assert enderworld != null;
        if (world.equals(world.getServer().getOverworld())) {
            otherWorld = enderworld;
        } else if (world.equals(enderworld)) {
            otherWorld = world.getServer().getOverworld();
        } else {
            return ActionResult.SUCCESS;
        }
        if (LinkEnderworldPortals.tryTakePortalItems(player)) {
            // temporary
            BlockPos otherRoot = new BlockPos(0, 80, 0);
            buildPlatformAndClearRoom(otherWorld, player, otherRoot);
            portal.getPlaceable().generate(otherWorld, otherRoot);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    /**
     * clears a 3x3x3 space and below build a platform if the player has enough items and there is not
     * already something to stand on. Will replace non-solid blocks like fence gates for obvious reasons.
     * Keep in mind, that the parameters have to be from the other side, where the portal will be build
     * @param world the dimension of the other side
     * @param player the player trying to build the portal
     * @param root the root position of the other portal
     */
    public static void buildPlatformAndClearRoom(ServerWorld world, ServerPlayerEntity player, BlockPos root) {
        // todo clear the room
        // todo check if creative
        List<ItemStack> platformStacks = new ArrayList<>();
        Set<Item> possiblePlatformItems = Sets.newHashSet(
                Items.END_STONE,
                Items.STONE,
                Items.GRANITE,
                Items.DIORITE,
                Items.ANDESITE
        );
        for (int i = 0; i < player.getInventory().size(); ++i) {
            ItemStack itemStack = player.getInventory().getStack(i);
            if(possiblePlatformItems.contains(itemStack.getItem())) {
                platformStacks.add(itemStack);
            }
        }
        for (Vec3i offset : LinkEnderworldPortals.iteratePlatformPositions(world.getRandom())) {
            BlockPos pos = root.add(offset);
            if (world.getBlockState(pos).isSideSolidFullSquare(
                    world,
                    pos,
                    Direction.UP
            )) {
                continue;
            }
            if (platformStacks.isEmpty()) {
                break;
            }
            BlockState state = ((BlockItem)platformStacks.get(0).split(1).getItem())
                    .getBlock()
                    .getDefaultState();
            if (platformStacks.get(0).getCount() == 0) {
                platformStacks.remove(0);
            }
            Block.dropStacks(world.getBlockState(pos), world, pos);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
    }

    /**
     * tries to take only the portal items from the player, that is,
     * the glowstone and the two gold blocks (or the infused ones)
     * @param player the player how tries to activate the portal
     * @return true, if the items are taken
     */
    public static boolean tryTakePortalItems(ServerPlayerEntity player) {
        // I don't think you can anyhow make it usable in data packs, has to be hard coded
        // better would be a config though
        List<ItemStack> goldStacks = new ArrayList<>();
        @Nullable ItemStack glowstoneStack = null;
        // scraped and modified from net.minecraft.entity.player.PlayerEntity.getArrowType
        for (int i = 0; i < player.getInventory().size(); ++i) {
            ItemStack itemStack = player.getInventory().getStack(i);
            if (itemStack.getItem().equals(ModItems.INFUSED_GLOWSTONE)) {
                if (glowstoneStack == null) {
                    glowstoneStack = itemStack;
                }
            } else if (itemStack.getItem().equals(ModItems.INFUSED_GOLD_BLOCK)) {
                if (goldStacks.stream().mapToInt(ItemStack::getCount).sum() < 2) {
                    goldStacks.add(itemStack);
                }
            }
        }
        for (int i = 0; i < player.getInventory().size(); ++i) {
            ItemStack itemStack = player.getInventory().getStack(i);
            if (itemStack.getItem().equals(Items.GLOWSTONE)) {
                if (glowstoneStack == null) {
                    glowstoneStack = itemStack;
                }
            } else if (itemStack.getItem().equals(Items.GOLD_BLOCK)) {
                if (goldStacks.stream().mapToInt(ItemStack::getCount).sum() < 2) {
                    goldStacks.add(itemStack);
                }
            }
        }

        if (glowstoneStack != null) {
            if (goldStacks.stream().mapToInt(ItemStack::getCount).sum() >= 2) {
                glowstoneStack.split(1);
                ItemStack goldStack1 = goldStacks.get(0);
                if (goldStack1.getCount() == 1) {
                    goldStack1.split(1);
                    goldStacks.get(1).split(1);
                } else {
                    goldStack1.split(2);
                }
                return true;
            }
        }
        return false;
    }
    protected static List<Vec3i> iteratePlatformPositions(Random random) {
        List<Vec3i> result = new ArrayList<>();
        // configure yoff
        int yoff = -2;
        // configure depth
        for (int depth = 0; depth <= 3; depth++) {
            // Set to have no duplication
            Set<Vec3i> unshuffledSet = new HashSet<>();
            for (int iterCoord = -depth; iterCoord <= depth; iterCoord++) {
                unshuffledSet.add(new Vec3i(iterCoord, yoff, -depth));
                unshuffledSet.add(new Vec3i(iterCoord, yoff, depth));
                unshuffledSet.add(new Vec3i(-depth, yoff, iterCoord));
                unshuffledSet.add(new Vec3i(depth, yoff, iterCoord));
            }
            List<Vec3i> toShuffledRing = new ArrayList<>(unshuffledSet);
            Collections.shuffle(toShuffledRing, random);
            result.addAll(toShuffledRing);
        }
        return result;
    }
}
