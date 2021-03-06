package net.minecraft.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import uprizing.ToggleSprint;

public class EntityPlayerSP extends AbstractClientPlayer {

    public MovementInputFromOptions movementInput;
    protected Minecraft mc;

    /**
     * Used to tell if the player pressed forward twice. If this is at 0 and it's pressed (And they are allowed to
     * sprint, aka enough food on the ground etc) it sets this to 7. If it's pressed and it's greater than 0 enable
     * sprinting.
     */
    protected int sprintToggleTimer;

    /**
     * Ticks left before sprinting is disabled.
     */
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;

    /**
     * The amount of time an entity has been in a Portal
     */
    public float timeInPortal;

    /**
     * The amount of time an entity has been in a Portal the previous tick
     */
    public float prevTimeInPortal;

    public EntityPlayerSP(Minecraft p_i1238_1_, World p_i1238_2_, Session p_i1238_3_, int p_i1238_4_) {
        super(p_i1238_2_, p_i1238_3_.getProfile());
        this.mc = p_i1238_1_;
        this.dimension = p_i1238_4_;
    }

    public void updateEntityActionState() {
        super.updateEntityActionState();
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
        this.prevRenderArmYaw = this.renderArmYaw;
        this.prevRenderArmPitch = this.renderArmPitch;
        this.renderArmPitch = (float) ((double) this.renderArmPitch + (double) (this.rotationPitch - this.renderArmPitch) * 0.5D);
        this.renderArmYaw = (float) ((double) this.renderArmYaw + (double) (this.rotationYaw - this.renderArmYaw) * 0.5D);
    }

    @Override
    public final void jump() {
        this.motionY = 0.41999998688697815D;

        final boolean magic;

        if (magic = this.isPotionActive(Potion.jump)) {
            this.motionY += (double) ((float) (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
        }

        final boolean sprinting;

        if (sprinting = this.isSprinting()) {
            float var1 = this.rotationYaw * 0.017453292F;
            this.motionX -= (double) (MathHelper.sin(var1) * 0.2F);
            this.motionZ += (double) (MathHelper.cos(var1) * 0.2F);
        }

        final ToggleSprint toggleSprint = mc.uprizing.getToggleSprint();

        //if (magic) {
            //toggleSprint.mode = ToggleSprint.JUMPING_MAGIC;
        //} else if (sprinting) {
            //toggleSprint.mode = ToggleSprint.JUMPING_SPRINTING;
        //} else if (toggleSprint.alwaysJumping) {
            //toggleSprint.mode = ToggleSprint.JUMPING_TOGGLED;
        //} else {
            //toggleSprint.mode = ToggleSprint.JUMPING_VANILLA;
        //}

        this.isAirBorne = test = true;
    }

    public boolean test; // TODO: temporary

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        if (test && onGround) {
            test = false;
        }

        if (this.sprintingTicksLeft > 0) {
            --this.sprintingTicksLeft;

            if (this.sprintingTicksLeft == 0) {
                this.setSprinting(false);
            }
        }

        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }

        if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
            this.posX = this.posZ = 0.5D;
            this.posX = 0.0D;
            this.posZ = 0.0D;
            this.rotationYaw = (float) this.ticksExisted / 12.0F;
            this.rotationPitch = 10.0F;
            this.posY = 68.5D;
        } else {
            this.prevTimeInPortal = this.timeInPortal;

            if (this.inPortal) {
                if (this.mc.currentScreen != null) {
                    this.mc.displayGuiScreen(null);
                }

                if (this.timeInPortal == 0.0F) {
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
                }

                this.timeInPortal += 0.0125F;

                if (this.timeInPortal >= 1.0F) {
                    this.timeInPortal = 1.0F;
                }

                this.inPortal = false;
            } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
                this.timeInPortal += 0.006666667F;

                if (this.timeInPortal > 1.0F) {
                    this.timeInPortal = 1.0F;
                }
            } else {
                if (this.timeInPortal > 0.0F) {
                    this.timeInPortal -= 0.05F;
                }

                if (this.timeInPortal < 0.0F) {
                    this.timeInPortal = 0.0F;
                }
            }

            if (this.timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }

            boolean isJumping = this.movementInput.jump;
            float minSpeed = 0.8F;
            boolean isMovingForward = this.movementInput.moveForward >= minSpeed;
            this.movementInput.updatePlayerMoveState();

            if (this.isUsingItem() && !this.isRiding()) {
                this.movementInput.moveStrafe *= 0.2F;
                this.movementInput.moveForward *= 0.2F;
                this.sprintToggleTimer = 0;
            }

            if (this.movementInput.sneak && this.ySize < 0.2F) {
                this.ySize = 0.2F;
            }

            this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double) this.width * 0.35D);
            this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double) this.width * 0.35D);
            this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double) this.width * 0.35D);
            this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double) this.width * 0.35D);
            boolean enoughHunger = (float) this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

            this.handleSprintSystem(minSpeed, isMovingForward, enoughHunger);

            if (this.capabilities.allowFlying && !isJumping && this.movementInput.jump) { // TODO: 155
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                } else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }

            if (this.capabilities.isFlying) {
                if (this.movementInput.sneak) {
                    this.motionY -= 0.15D;
                }

                if (this.movementInput.jump) {
                    this.motionY += 0.15D;
                }
            }

            if (this.isRidingHorse()) {
                if (this.horseJumpPowerCounter < 0) {
                    ++this.horseJumpPowerCounter;

                    if (this.horseJumpPowerCounter == 0) {
                        this.horseJumpPower = 0.0F;
                    }
                }

                if (isJumping && !this.movementInput.jump) {
                    this.horseJumpPowerCounter = -10;
                    this.sendHorseJump();
                } else if (!isJumping && this.movementInput.jump) {
                    this.horseJumpPowerCounter = 0;
                    this.horseJumpPower = 0.0F;
                } else if (isJumping) {
                    ++this.horseJumpPowerCounter;

                    if (this.horseJumpPowerCounter < 10) {
                        this.horseJumpPower = (float) this.horseJumpPowerCounter * 0.1F;
                    } else {
                        this.horseJumpPower = 0.8F + 2.0F / (float) (this.horseJumpPowerCounter - 9) * 0.1F;
                    }
                }
            } else {
                this.horseJumpPower = 0.0F;
            }

            super.onLivingUpdate();

            if (this.onGround && this.capabilities.isFlying) {
                this.capabilities.isFlying = false;
                this.sendPlayerAbilities();
            }
        }
    }

    private void handleSprintSystem(float minSpeed, boolean isMovingForward, boolean enoughHunger) {
        final ToggleSprint toggleSprint = mc.uprizing.getToggleSprint();

        if ((toggleSprint.optionKeepSprint) && (!movementInput.sprint)) {
            movementInput.sprint = true;
        }

        final boolean isSprintDisabled = !toggleSprint.alwaysSprinting;
        final boolean canDoubleTap = toggleSprint.doubleTapping;

        if (toggleSprint.wasSprintDisabled) {
            this.setSprinting(false);
            movementInput.updateSprint(false, false);
            toggleSprint.wasSprintDisabled = false;
        }

        if (isSprintDisabled) { // Default Sprint routine converted to PlayerAPI, use if ToggleSprint is disabled
            if (toggleSprint.doubleTapping && onGround && !isMovingForward && movementInput.moveForward >= minSpeed && !this.isSprinting() && enoughHunger && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
                if (sprintToggleTimer <= 0 && !mc.keyBindings.sprint.getIsKeyPressed()) {
                    sprintToggleTimer = 7;
                } else {
                    this.setSprinting(true);
                    movementInput.updateSprint(true, false);
                    toggleSprint.optionKeepSprint = true;
                }
            }

            if (!this.isSprinting() && movementInput.moveForward >= minSpeed && enoughHunger && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.keyBindings.sprint.getIsKeyPressed()) {
                this.setSprinting(true);
                movementInput.updateSprint(true, false);
                toggleSprint.optionKeepSprint = true;
            }
        } else {
            final boolean state = movementInput.sprint;


            /*
             * Only handle changes in state under the following conditions:
             * - On ground, not hungry, not eating/using item, not blind, and not Vanilla
             *
             * 5/6/14 - onGround check removed to match vanilla's 'start sprint while jumping' behavior.
             * if(this.player.onGround && enoughHunger && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness) && !this.customMovementInput.sprintHeldAndReleased)
             */
            if (enoughHunger && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && !movementInput.sprintHeldAndReleased) {
                if (!canDoubleTap || !this.isSprinting()) {
                    this.setSprinting(state);
                }
            }

            if (canDoubleTap && !state && onGround && !isMovingForward && movementInput.moveForward >= minSpeed && !this.isSprinting() && enoughHunger && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
                if (this.sprintToggleTimer == 0) {
                    this.sprintToggleTimer = 7;
                } else {
                    this.setSprinting(true);
                    movementInput.updateSprint(true, true);
                    this.sprintToggleTimer = 0;
                }
            }
        }

        /*
         * If sprinting, break the sprint in appropriate circumstances:
         * - Player stops moving forward, runs into something, or gets too hungry
         */
        if (this.isSprinting() && (movementInput.moveForward < minSpeed || isCollidedHorizontally || !enoughHunger)) {
            this.setSprinting(false);

            // Undo toggle if we resumed vanilla operation due to Hold&Release, DoubleTap, Fly, Ride
            if (movementInput.sprintHeldAndReleased || isSprintDisabled || movementInput.sprintDoubleTapped || capabilities.isFlying || this.isRiding()) {
                movementInput.updateSprint(false, false);
                toggleSprint.optionKeepSprint = true;
            } else {
                toggleSprint.optionKeepSprint = true;
            }
        }

        if (toggleSprint.optionEnableFlyBoost && capabilities.isFlying && this.mc.keyBindings.sprint.getIsKeyPressed() && this.capabilities.isCreativeMode) {
            capabilities.setFlySpeed(0.05F * (float) toggleSprint.optionFlyBoostAmount);

            if (movementInput.sneak)
                motionY -= 0.15D * toggleSprint.optionFlyBoostAmount;
            if (movementInput.jump)
                motionY += 0.15D * toggleSprint.optionFlyBoostAmount;

        } else if (capabilities.getFlySpeed() != 0.05F) {
            capabilities.setFlySpeed(0.05F);
        }
    }

    /**
     * Gets the player's field of view multiplier. (ex. when flying)
     */
    public float getFOVMultiplier() {
        float var1 = 1.0F;

        if (this.capabilities.isFlying) {
            var1 *= 1.1F;
        }

        IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        var1 = (float) ((double) var1 * ((var2.getAttributeValue() / (double) this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1) || Float.isInfinite(var1)) {
            var1 = 1.0F;
        }

        if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
            int var3 = this.getItemInUseDuration();
            float var4 = (float) var3 / 20.0F;

            if (var4 > 1.0F) {
                var4 = 1.0F;
            } else {
                var4 *= var4;
            }

            var1 *= 1.0F - var4 * 0.15F;
        }

        return var1;
    }

    /**
     * set current crafting inventory back to the 2x2 square
     */
    public void closeScreen() {
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }

    public void displayGUIEditSign(TileEntity p_146100_1_) {
        if (p_146100_1_ instanceof TileEntitySign) {
            this.mc.displayGuiScreen(new GuiEditSign((TileEntitySign) p_146100_1_));
        } else if (p_146100_1_ instanceof TileEntityCommandBlock) {
            this.mc.displayGuiScreen(new GuiCommandBlock(((TileEntityCommandBlock) p_146100_1_).func_145993_a()));
        }
    }

    public void func_146095_a(CommandBlockLogic p_146095_1_) {
        this.mc.displayGuiScreen(new GuiCommandBlock(p_146095_1_));
    }

    /**
     * Displays the GUI for interacting with a book.
     */
    public void displayGUIBook(ItemStack p_71048_1_) {
        Item var2 = p_71048_1_.getItem();

        if (var2 == Items.written_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, p_71048_1_, false));
        } else if (var2 == Items.writable_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, p_71048_1_, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory p_71007_1_) {
        this.mc.displayGuiScreen(new GuiChest(this.inventory, p_71007_1_));
    }

    public void func_146093_a(TileEntityHopper p_146093_1_) {
        this.mc.displayGuiScreen(new GuiHopper(this.inventory, p_146093_1_));
    }

    public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_) {
        this.mc.displayGuiScreen(new GuiHopper(this.inventory, p_96125_1_));
    }

    public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, p_110298_2_, p_110298_1_));
    }

    /**
     * Displays the crafting GUI for a workbench.
     */
    public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_, int p_71058_3_) {
        this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, p_71058_1_, p_71058_2_, p_71058_3_));
    }

    public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_, int p_71002_3_, String p_71002_4_) {
        this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, p_71002_1_, p_71002_2_, p_71002_3_, p_71002_4_));
    }

    /**
     * Displays the GUI for interacting with an anvil.
     */
    public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_) {
        this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj, p_82244_1_, p_82244_2_, p_82244_3_));
    }

    public void func_146101_a(TileEntityFurnace p_146101_1_) {
        this.mc.displayGuiScreen(new GuiFurnace(this.inventory, p_146101_1_));
    }

    public void func_146098_a(TileEntityBrewingStand p_146098_1_) {
        this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, p_146098_1_));
    }

    public void func_146104_a(TileEntityBeacon p_146104_1_) {
        this.mc.displayGuiScreen(new GuiBeacon(this.inventory, p_146104_1_));
    }

    public void func_146102_a(TileEntityDispenser p_146102_1_) {
        this.mc.displayGuiScreen(new GuiDispenser(this.inventory, p_146102_1_));
    }

    public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, p_71030_1_, this.worldObj, p_71030_2_));
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity p_71009_1_) {
        this.mc.effectRenderer.addEffect(new EntityCrit2FX(this.mc.theWorld, p_71009_1_));
    }

    public void onEnchantmentCritical(Entity p_71047_1_) {
        EntityCrit2FX var2 = new EntityCrit2FX(this.mc.theWorld, p_71047_1_, "magicCrit");
        this.mc.effectRenderer.addEffect(var2);
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
        this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, p_71001_1_, this, -0.5F));
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking() {
        return this.movementInput.sneak && !this.sleeping;
    }

    /**
     * Updates health locally.
     */
    public void setPlayerSPHealth(float p_71150_1_) {
        float var2 = this.getHealth() - p_71150_1_;

        if (var2 <= 0.0F) {
            this.setHealth(p_71150_1_);

            if (var2 < 0.0F) {
                this.hurtResistantTime = this.maxHurtResistantTime / 2;
            }
        } else {
            this.lastDamage = var2;
            this.setHealth(this.getHealth());
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(DamageSource.generic, var2);
            this.hurtTime = this.maxHurtTime = 10;
        }
    }

    public void addChatComponentMessage(IChatComponent p_146105_1_) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(p_146105_1_);
    }

    private boolean isBlockTranslucent(int p_71153_1_, int p_71153_2_, int p_71153_3_) {
        return this.worldObj.getBlock(p_71153_1_, p_71153_2_, p_71153_3_).isNormalCube();
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        int var7 = MathHelper.floor_double(x);
        int var8 = MathHelper.floor_double(y);
        int var9 = MathHelper.floor_double(z);
        double var10 = x - (double) var7;
        double var12 = z - (double) var9;

        if (this.isBlockTranslucent(var7, var8, var9) || this.isBlockTranslucent(var7, var8 + 1, var9)) {
            boolean var14 = !this.isBlockTranslucent(var7 - 1, var8, var9) && !this.isBlockTranslucent(var7 - 1, var8 + 1, var9);
            boolean var15 = !this.isBlockTranslucent(var7 + 1, var8, var9) && !this.isBlockTranslucent(var7 + 1, var8 + 1, var9);
            boolean var16 = !this.isBlockTranslucent(var7, var8, var9 - 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 - 1);
            boolean var17 = !this.isBlockTranslucent(var7, var8, var9 + 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 + 1);
            byte var18 = -1;
            double var19 = 9999.0D;

            if (var14 && var10 < var19) {
                var19 = var10;
                var18 = 0;
            }

            if (var15 && 1.0D - var10 < var19) {
                var19 = 1.0D - var10;
                var18 = 1;
            }

            if (var16 && var12 < var19) {
                var19 = var12;
                var18 = 4;
            }

            if (var17 && 1.0D - var12 < var19) {
                var19 = 1.0D - var12;
                var18 = 5;
            }

            float var21 = 0.1F;

            if (var18 == 0) {
                this.motionX = (double) (-var21);
            } else if (var18 == 1) {
                this.motionX = (double) var21;
            } else if (var18 == 4) {
                this.motionZ = (double) (-var21);
            } else if (var18 == 5) {
                this.motionZ = (double) var21;
            }
        }

        return false;
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = sprinting ? 600 : 0;
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    public void setXPStats(float p_71152_1_, int p_71152_2_, int p_71152_3_) {
        this.experience = p_71152_1_;
        this.experienceTotal = p_71152_2_;
        this.experienceLevel = p_71152_3_;
    }

    /**
     * Notifies this sender of some sort of information.  This is for messages intended to display to the user.  Used
     * for typical output (like "you asked for whether or not this game rule is set, so here's your answer"), warnings
     * (like "I fetched this block for you by ID, but I'd like you to know that every time you do this, I die a little
     * inside"), and errors (like "it's not called iron_pixacke, silly").
     */
    public void addChatMessage(IChatComponent message) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(message);
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return permissionLevel <= 0;
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX + 0.5D), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ + 0.5D));
    }

    public void playSound(String name, float volume, float pitch) {
        this.worldObj.playSound(this.posX, this.posY - (double) this.yOffset, this.posZ, name, volume, pitch, false);
    }

    /**
     * Returns whether the entity is in a local (client) world
     */
    public boolean isClientWorld() {
        return true;
    }

    public boolean isRidingHorse() {
        return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse;
    }

    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }

    protected void sendHorseJump() {}
}