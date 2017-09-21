package com.lemeng.common;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 15:39
 */
public class Const {

    /*****************SystemCommand****1-100****************/

    public static final short HeartBreakCommand = 1;


    /************UserCommand******101-500*********/

    public static final short LoginCommand = 101;
    public static final short LoginResultCommand = 102;
    public static final short RegistCommand = 103;
    public static final short SendVerifyCodeCommand = 104;
    public static final short InitPlayerCommand = 105;
    public static final short InitPlayerResultCommand = 106;
    public static final short UserInfoCommand = 107;


    /**********************GameCommand*****500-1000*********************/

    public static final short CreateTeamCommand = 501;
    public static final short InviteGameCommand = 502;
    public static final short InvitedGameCommand = 503;
    public static final short RefuseInviteGameCommand = 504;
    public static final short AcceptInviteGameCommand = 505;
    public static final short TeamCommand = 506;
    public static final short RefuseGameCommand = 507;
    public static final short FindGameCommand = 508;
    public static final short FindedGameCommand = 509;
    public static final short PlayerInfoCommand = 510;
    public static final short JumpCommand = 511;
    public static final short EatCommand = 512;
    public static final short ThrowBoxCommand = 513;
    public static final short HideBoxCommand = 514;
    public static final short HitCommand = 515;
    public static final short QuitGameCommand = 516;
    public static final short GameOverCommand = 517;
    public static final short GameMessageCommand = 518;

}
