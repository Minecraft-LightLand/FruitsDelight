package dev.xkmc.fruitsdelight.compat.create;

import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class CreateVersionChecker {

	public static boolean isCreate6() {
		ModFileInfo create = LoadingModList.get().getModFileById("create");
		if (create == null) return false;
		return create.getMods().stream().anyMatch(modInfo -> modInfo.getModId().equals("create")
				&& modInfo.getVersion().compareTo(new DefaultArtifactVersion("6.0.0")) >= 0);
	}

}
