package net.minecraft.client.resources;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @deprecated Forge: {@link net.minecraftforge.client.resource.ISelectiveResourceReloadListener}, which selectively allows
 * individual resource types being reloaded should rather be used where possible.
 */
@Deprecated
@SideOnly(Side.CLIENT)
public interface IResourceManagerReloadListener
{
    void onResourceManagerReload(IResourceManager resourceManager);
}