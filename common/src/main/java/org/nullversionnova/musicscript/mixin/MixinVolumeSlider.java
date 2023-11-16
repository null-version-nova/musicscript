package org.nullversionnova.musicscript.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.nullversionnova.musicscript.SoundManager;

/**
 * An example mixin. Mixins cannot be written with kotlin!
 * [<a href="https://github.com/SpongePowered/Mixin/issues/245">Github Issue</a>]
 */
@Mixin(Options.class)
public class MixinVolumeSlider {
    @Inject(at = @At("TAIL"), method = "setSoundCategoryVolume(Lnet/minecraft/sounds/SoundSource;F)V")
    private void setSoundCategoryVolume(SoundSource soundSource, float f, CallbackInfo info) {
        Options options = Minecraft.getInstance().options;
        SoundManager.setVolume(options.getSoundSourceVolume(SoundSource.MUSIC) * options.getSoundSourceVolume(SoundSource.MASTER));
    }
}