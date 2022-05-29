package com.github.jootnet.m2.core.actor;

/**
 * 人物衣服在各个动作和方向下的贴图信息
 */
public final class HumActionInfos {
	/** 人物朝正北(上)方站立时的贴图信息 */
    public static HumActionInfo StandNorth;
	/** 人物朝东北(右上)方站立时的贴图信息 */
    public static HumActionInfo StandNorthEast;
	/** 人物朝正东(右)方站立时的贴图信息 */
    public static HumActionInfo StandEast;
	/** 人物朝东南(右下)站立时的贴图信息 */
    public static HumActionInfo StandSouthEast;
	/** 人物朝南(下)方站立时的贴图信息 */
    public static HumActionInfo StandSouth;
	/** 人物朝西南(左下)方站立时的贴图信息 */
    public static HumActionInfo StandSouthWest;
	/** 人物朝西(左)站立时的贴图信息 */
    public static HumActionInfo StandWest;
	/** 人物朝西北(左上)方站立时的贴图信息 */
    public static HumActionInfo StandNorthWest;
    
	/** 人物朝正北(上)方走动时的贴图信息 */
    public static HumActionInfo WalkNorth;
	/** 人物朝东北(右上)方走动时的贴图信息 */
    public static HumActionInfo WalkNorthEast;
	/** 人物朝正东(右)方走动时的贴图信息 */
    public static HumActionInfo WalkEast;
	/** 人物朝东南(右下)走动时的贴图信息 */
    public static HumActionInfo WalkSouthEast;
	/** 人物朝南(下)方走动时的贴图信息 */
    public static HumActionInfo WalkSouth;
	/** 人物朝西南(左下)方走动时的贴图信息 */
    public static HumActionInfo WalkSouthWest;
	/** 人物朝西(左)走动时的贴图信息 */
    public static HumActionInfo WalkWest;
	/** 人物朝西北(左上)方走动时的贴图信息 */
    public static HumActionInfo WalkNorthWest;
    
	/** 人物朝正北(上)方跑动时的贴图信息 */
    public static HumActionInfo RunNorth;
	/** 人物朝东北(右上)方跑动时的贴图信息 */
    public static HumActionInfo RunNorthEast;
	/** 人物朝正东(右)方跑动时的贴图信息 */
    public static HumActionInfo RunEast;
	/** 人物朝东南(右下)跑动时的贴图信息 */
    public static HumActionInfo RunSouthEast;
	/** 人物朝南(下)方跑动时的贴图信息 */
    public static HumActionInfo RunSouth;
	/** 人物朝西南(左下)方跑动时的贴图信息 */
    public static HumActionInfo RunSouthWest;
	/** 人物朝西(左)跑动时的贴图信息 */
    public static HumActionInfo RunWest;
	/** 人物朝西北(左上)方跑动时的贴图信息 */
    public static HumActionInfo RunNorthWest;
    
    /**
     * 通过方向获取站立动作
     * 
     * @param dir 角色朝向
     * @return 站立动作
     */
    public static HumActionInfo stand(Direction dir) {
    	switch (dir) {
		case North:
			return StandNorth;
		case NorthEast:
			return StandNorthEast;
		case East:
			return StandEast;
		case SouthEast:
			return StandSouthEast;
		case South:
			return StandSouth;
		case SouthWest:
			return StandSouthWest;
		case West:
			return StandWest;
		case NorthWest:
		default:
			return StandNorthWest;
		}
    }

    /**
     * 通过方向获取走动动作
     * 
     * @param dir 角色朝向
     * @return 走动动作
     */
    public static HumActionInfo walk(Direction dir) {
    	switch (dir) {
		case North:
			return WalkNorth;
		case NorthEast:
			return WalkNorthEast;
		case East:
			return WalkEast;
		case SouthEast:
			return WalkSouthEast;
		case South:
			return WalkSouth;
		case SouthWest:
			return WalkSouthWest;
		case West:
			return WalkWest;
		case NorthWest:
		default:
			return WalkNorthWest;
		}
    }

    /**
     * 通过方向获取跑动动作
     * 
     * @param dir 角色朝向
     * @return 跑动动作
     */
    public static HumActionInfo run(Direction dir) {
    	switch (dir) {
		case North:
			return RunNorth;
		case NorthEast:
			return RunNorthEast;
		case East:
			return RunEast;
		case SouthEast:
			return RunSouthEast;
		case South:
			return RunSouth;
		case SouthWest:
			return RunSouthWest;
		case West:
			return RunWest;
		case NorthWest:
		default:
			return RunNorthWest;
		}
    }
    
    static {
    	StandNorth = new HumActionInfo();
    	StandNorth.frameIdx = 0;
    	StandNorth.frameCount = 8;
    	//StandNorth.skipCount = 0;
    	StandNorth.duration = 200;
    	StandNorth.act = Action.Stand;
    	StandNorth.dir = Direction.North;
    	
    	StandNorthEast = new HumActionInfo();
    	StandNorthEast.frameIdx = 8;
    	StandNorthEast.frameCount = 8;
    	StandNorthEast.duration = 200;
    	StandNorthEast.act = Action.Stand;
    	StandNorthEast.dir = Direction.NorthEast;
    	
    	StandEast = new HumActionInfo();
    	StandEast.frameIdx = 16;
    	StandEast.frameCount = 8;
    	StandEast.duration = 200;
    	StandEast.act = Action.Stand;
    	StandEast.dir = Direction.East;
    	
    	StandSouthEast = new HumActionInfo();
    	StandSouthEast.frameIdx = 24;
    	StandSouthEast.frameCount = 8;
    	StandSouthEast.duration = 200;
    	StandSouthEast.act = Action.Stand;
    	StandSouthEast.dir = Direction.SouthEast;
    	
    	StandSouth = new HumActionInfo();
    	StandSouth.frameIdx = 32;
    	StandSouth.frameCount = 8;
    	StandSouth.duration = 200;
    	StandSouth.act = Action.Stand;
    	StandSouth.dir = Direction.South;
    	
    	StandSouthWest = new HumActionInfo();
    	StandSouthWest.frameIdx = 40;
    	StandSouthWest.frameCount = 8;
    	StandSouthWest.duration = 200;
    	StandSouthWest.act = Action.Stand;
    	StandSouthWest.dir = Direction.SouthWest;
    	
    	StandWest = new HumActionInfo();
    	StandWest.frameIdx = 48;
    	StandWest.frameCount = 8;
    	StandWest.duration = 200;
    	StandWest.act = Action.Stand;
    	StandWest.dir = Direction.West;
    	
    	StandNorthWest = new HumActionInfo();
    	StandNorthWest.frameIdx = 56;
    	StandNorthWest.frameCount = 8;
    	StandNorthWest.duration = 200;
    	StandNorthWest.act = Action.Stand;
    	StandNorthWest.dir = Direction.NorthWest;
        
    	

    	WalkNorth = new HumActionInfo();
    	WalkNorth.frameIdx = 64;
    	WalkNorth.frameCount = 6;
    	//WalkNorth.skipCount = 2;
    	WalkNorth.duration = 90;
    	WalkNorth.act = Action.Walk;
    	WalkNorth.dir = Direction.North;
    	
    	WalkNorthEast = new HumActionInfo();
    	WalkNorthEast.frameIdx = 72;
    	WalkNorthEast.frameCount = 6;
    	WalkNorthEast.duration = 90;
    	WalkNorthEast.act = Action.Walk;
    	WalkNorthEast.dir = Direction.NorthEast;
    	
    	WalkEast = new HumActionInfo();
    	WalkEast.frameIdx = 80;
    	WalkEast.frameCount = 6;
    	WalkEast.duration = 90;
    	WalkEast.act = Action.Walk;
    	WalkEast.dir = Direction.East;
    	
    	WalkSouthEast = new HumActionInfo();
    	WalkSouthEast.frameIdx = 88;
    	WalkSouthEast.frameCount = 6;
    	WalkSouthEast.duration = 90;
    	WalkSouthEast.act = Action.Walk;
    	WalkSouthEast.dir = Direction.SouthEast;
    	
    	WalkSouth = new HumActionInfo();
    	WalkSouth.frameIdx = 96;
    	WalkSouth.frameCount = 6;
    	WalkSouth.duration = 90;
    	WalkSouth.act = Action.Walk;
    	WalkSouth.dir = Direction.South;
    	
    	WalkSouthWest = new HumActionInfo();
    	WalkSouthWest.frameIdx = 104;
    	WalkSouthWest.frameCount = 6;
    	WalkSouthWest.duration = 90;
    	WalkSouthWest.act = Action.Walk;
    	WalkSouthWest.dir = Direction.SouthWest;
    	
    	WalkWest = new HumActionInfo();
    	WalkWest.frameIdx = 112;
    	WalkWest.frameCount = 6;
    	WalkWest.duration = 90;
    	WalkWest.act = Action.Walk;
    	WalkWest.dir = Direction.West;
    	
    	WalkNorthWest = new HumActionInfo();
    	WalkNorthWest.frameIdx = 120;
    	WalkNorthWest.frameCount = 6;
    	WalkNorthWest.duration = 90;
    	WalkNorthWest.act = Action.Walk;
    	WalkNorthWest.dir = Direction.NorthWest;
        
    	

    	RunNorth = new HumActionInfo();
    	RunNorth.frameIdx = 128;
    	RunNorth.frameCount = 6;
    	//RunNorth.skipCount = 2;
    	RunNorth.duration = 100;
    	RunNorth.act = Action.Run;
    	RunNorth.dir = Direction.North;
    	
    	RunNorthEast = new HumActionInfo();
    	RunNorthEast.frameIdx = 136;
    	RunNorthEast.frameCount = 6;
    	RunNorthEast.duration = 100;
    	RunNorthEast.act = Action.Run;
    	RunNorthEast.dir = Direction.NorthEast;
    	
    	RunEast = new HumActionInfo();
    	RunEast.frameIdx = 144;
    	RunEast.frameCount = 6;
    	RunEast.duration = 100;
    	RunEast.act = Action.Run;
    	RunEast.dir = Direction.East;
    	
    	RunSouthEast = new HumActionInfo();
    	RunSouthEast.frameIdx = 152;
    	RunSouthEast.frameCount = 6;
    	RunSouthEast.duration = 100;
    	RunSouthEast.act = Action.Run;
    	RunSouthEast.dir = Direction.SouthEast;
    	
    	RunSouth = new HumActionInfo();
    	RunSouth.frameIdx = 160;
    	RunSouth.frameCount = 6;
    	RunSouth.duration = 100;
    	RunSouth.act = Action.Run;
    	RunSouth.dir = Direction.South;
    	
    	RunSouthWest = new HumActionInfo();
    	RunSouthWest.frameIdx = 168;
    	RunSouthWest.frameCount = 6;
    	RunSouthWest.duration = 100;
    	RunSouthWest.act = Action.Run;
    	RunSouthWest.dir = Direction.SouthWest;
    	
    	RunWest = new HumActionInfo();
    	RunWest.frameIdx = 176;
    	RunWest.frameCount = 6;
    	RunWest.duration = 100;
    	RunWest.act = Action.Run;
    	RunWest.dir = Direction.West;
    	
    	RunNorthWest = new HumActionInfo();
    	RunNorthWest.frameIdx = 184;
    	RunNorthWest.frameCount = 6;
    	RunNorthWest.duration = 100;
    	RunNorthWest.act = Action.Run;
    	RunNorthWest.dir = Direction.NorthWest;
    }
}
