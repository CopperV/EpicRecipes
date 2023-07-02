package me.Vark123.EpicRecipes.FileSystem;

public final class FileManager {

	private static final FileManager inst = new FileManager();
	
	private boolean init = false;
	
	private FileManager() {
		
	}
	
	public static final FileManager get() {
		return inst;
	}
	
	public void init() {
		if(init)
			return;
		init = true;
	}
	
}
