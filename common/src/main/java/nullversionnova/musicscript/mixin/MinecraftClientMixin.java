package nullversionnova.musicscript.mixin;

import net.minecraft.client.MinecraftClient;
import nullversionnova.musicscript.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "openPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;pauseAll()V"))
    void RedirectPause(net.minecraft.client.sound.SoundManager instance) {
        SoundManager.pause();
    }

    @Redirect(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;resumeAll()V"))
    void RedirectResume(net.minecraft.client.sound.SoundManager instance) {
        SoundManager.resume();
    }
}
