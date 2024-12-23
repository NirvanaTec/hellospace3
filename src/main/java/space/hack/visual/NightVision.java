/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.hack.visual;

import space.hack.Hack;
import space.hack.HackCategory;
import space.utils.Utils;
import space.utils.Wrapper;
import space.value.Mode;
import space.value.ModeValue;

public class NightVision extends Hack
{
    public final ModeValue mode;
    boolean usedB = false;
    boolean usedE = false;

    public NightVision() {
        super("NightVision", HackCategory.Visual);
        this.mode = new ModeValue("Mode", new Mode("Brightness"), new Mode("Effect", true));
        this.addValue(this.mode);
    }

    @Override
    public void onDisable() {
        if (usedB || this.mode.getValue("Brightness").isToggled()) {
            usedB = false;
            Wrapper.mc().options.gamma = 1.0f;
        }
        if(usedE || this.mode.getValue("Effect").isToggled()) {
            Utils.removeEffect(16);
            usedE = false;
        }
    }

    @Override
    public void onAllTick() {
        if (this.mode.getValue("Brightness").isToggled()) {
            if (usedE){
                Utils.removeEffect(16);
            }
            Wrapper.mc().options.gamma = 10.0f;
            usedB = true;
        } else {
            if (usedB){
                Wrapper.mc().options.gamma = 1.0f;
            }
            Utils.addEffect(16, 1000, 3);
            usedE = true;
        }
    }
}