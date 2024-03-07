package nullversionnova.musicscript.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.sound.SoundCategory;
import nullversionnova.musicscript.sound.RawSoundEngine;
import nullversionnova.musicscript.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * An example mixin. Mixins cannot be written with kotlin!
 * [<a href="https://github.com/SpongePowered/Mixin/issues/245">Github Issue</a>]
 */
@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(at = @At("TAIL"), method = "setSoundVolume(Lnet/minecraft/sound/SoundCategory;F)V")
    private void setSoundCategoryVolume(SoundCategory soundCategory, float f, CallbackInfo info) {
        GameOptions options = MinecraftClient.getInstance().options;
        if (options.getSoundVolume(SoundCategory.MUSIC) == 0 && !SoundManager.isPaused()) {
            RawSoundEngine.stopSounds();
        }
        RawSoundEngine.setVolume(options.getSoundVolume(SoundCategory.MUSIC) * options.getSoundVolume(SoundCategory.MASTER));
    }
}