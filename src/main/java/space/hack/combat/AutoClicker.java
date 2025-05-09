/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.hack.combat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import space.hack.Hack;
import space.hack.HackCategory;
import space.hack.player.FastPlace;
import space.manager.HackManager;
import space.utils.TimerUtils;
import space.utils.Wrapper;
import space.value.BooleanValue;
import space.value.IntValue;
import space.value.Mode;
import space.value.ModeValue;

public class AutoClicker extends Hack {
    private final TimerUtils time;
    private final ModeValue mode;
    private final IntValue cps;
    private final BooleanValue onlySword;
    private final BooleanValue offBox;
    public int delay;

    public AutoClicker() {
        super("AutoClicker", HackCategory.Combat);
        this.time = new TimerUtils();
        this.mode = new ModeValue("Mode", new Mode("Both", true), new Mode("Right"), new Mode("Left"));
        this.cps = new IntValue("CPS", 9, 1, 20);
        this.onlySword = new BooleanValue("OnlySword", false);
        this.offBox = new BooleanValue("OffBox", false);
        this.addValue(this.mode, this.cps, this.onlySword, this.offBox);
    }

    public static boolean isHoldingSword() {
        ItemStack heldItem = Wrapper.player().getMainHandItem();
        if (heldItem.isEmpty()) return false;
        Item heldItemItem = heldItem.getItem();
        return heldItemItem instanceof SwordItem;
    }

    @Override
    public void onAllTick() {
        if (!HackManager.noAimAssist() && this.time.isDelayX(this.delay)) {
            if (this.onlySword.getValue() && !isHoldingSword() || this.offBox.getValue() && FastPlace.isHoldingBlock()) {
                return;
            }
            String text = this.mode.getMode();
            if (Wrapper.mc().options.keyAttack.isDown()) {
                if (text.equals("Right") || text.equals("Both")) {
                    click(Wrapper.mc().options.keyAttack.getKey());
                }
            } else if (Wrapper.mc().options.keyUse.isDown()) {
                if (text.equals("Left") || text.equals("Both")) {
                    click(Wrapper.mc().options.keyUse.getKey());
                }
            }
        }
    }

    private void click(final InputConstants.Key keyCode) {
        this.delay = (int) Math.round(1000.0 / this.cps.getValue());
        this.time.setLastMS();
        KeyMapping.click(keyCode);
    }

}