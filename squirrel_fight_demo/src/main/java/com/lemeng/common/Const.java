package com.lemeng.common;

/**
 * Description:命令常量
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 15:39
 */
public class Const {

    public static final String RoomPrefix = "Squirrel_Fight_Room_";
    public static final String GamePrefix = "Squirrel_Fight_Game_";
    public static final String TeamPrefix = "Squirrel_Fight_Team_";
    public static final String PlayerPrefix = "Squirrel_Fight_Player_";


    /*****************SystemCommand****1-100****************/

    public static final short HeartBreakCommand = 1;


    /************UserCommand******101-500*********/

    public static final short LoginRequestCommand = 101;
    public static final short LoginResponseCommand = 102;
    public static final short RegistRequestCommand = 103;
    public static final short SendVerifyCodeRequestCommand = 104;
    public static final short InitPlayerRequestCommand = 105;
    public static final short InitPlayerResponseCommand = 106;
    public static final short UserInfoCommand = 107;


    /**********************GameCommand*****501-1000*********************/

    public static final short CreateRoomRequestCommand = 501;
    public static final short CreateRoomResponseCommand = 502;
    public static final short InviteJoinGameRequestCommand = 503;
    public static final short InviteJoinGameResponseCommand = 504;
    public static final short BeInvitedJoinGameRequestCommand = 505;
    public static final short BeInvitedJoinGameResponseCommand = 506;
    public static final short RoomUpdateCommand = 507;
    public static final short RefuseGameRequestCommand = 508;
    public static final short RefuseGameResponseCommand = 509;
    public static final short FindGameRequestCommand = 510;
    public static final short FindedGameRequestCommand = 511;
    public static final short NewPlayerJoinGameRequestCommand = 512;
    public static final short PlayerInfoCommand = 513;

    public static final short JumpCommand = 514;
    public static final short EatCommand = 515;
    public static final short ThrowBoxCommand = 516;
    public static final short HideBoxCommand = 517;
    public static final short HitCommand = 518;
    public static final short QuitGameCommand = 519;
    public static final short GameOverCommand = 520;
    public static final short GameMessageCommand = 521;
    public static final short QuitRoomCommand = 521;

    /****************************MallCommand********1001-1200**************************/

    public static final short RechargeGemstoneRequestCommand = 501;
    public static final short RechargeGemstoneResponseCommand = 502;
    public static final short BuySkillRequestCommand = 503;
    public static final short BuySkillResponseCommand = 504;
    public static final short BuyPetRequestCommand = 505;
    public static final short BuyPetResponseCommand = 506;
    public static final short BuyFootPrintRequestCommand = 507;
    public static final short BuyFootPrintResponseCommand = 508;

}
