package project.scene.battle;

import project.scene.Scene;

public class FieldScene extends Scene {
	
	private static FieldScene fs;
	
	public static FieldScene getInstance() {
		if(fs==null)
			fs=new FieldScene();
		return fs;
	}
	
	private FieldScene() {
		
	}

}
