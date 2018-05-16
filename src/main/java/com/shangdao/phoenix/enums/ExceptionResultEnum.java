package com.shangdao.phoenix.enums;

public enum ExceptionResultEnum {
    
    UNKNOW_ERROR(-1,"未知错误"),
    SUCCESSFUL(0,"OK"),
    
    SMS_ERROR(1000,"短信验证码错误，请重新输入验证码"),
    
    NOMOBILE(1001,"没有手机号"),
    
    BAN_USER_AUTHORIZATION(1011,"禁止用户授权"),
    
    ACT_OBJECT_MUST_MACHINE(1021,"动作对象必须是状态机实体"),
    SAME_OBJECT_CODE_NOTSAME(1022,"同一个实体的动作不能相同code"),
    ACT_OBJECT_MUST_PROJECT(1023,"动作对象必须是项目实体,才能通知到"),
    NOT_EFFECTIVE_ROLECODE(1024,"不是有效的角色code"),
    
    COMPANY_NAME_EXIST(1031,"公司名称已存在,请重新输入"),
    APPLY_FORM_EXIST(1032,"您已提交过公司申请单、请等待审核结果"),
    INPUT_REFUSR_REASON(1033,"请填写拒绝原因"),
    USER_NOT_COMPANY(1034,"该用户不属于本公司"),
    DISCOUNT_NOT_HAVE(1035,"请填写价格系数"),
    SOME_ONE_USER_NAME(1036,"该公司名称已经被使用"),
    YOU_HAVE_COMPANY(1037,"您已经有所属公司，不能再申请"),
    
    USER_NOT_REGISTER(1041,"该用户尚未注册"),
    
    FILE_UPLOAD_ALIYUN_ERROR(1051,"文件上传到阿里云出错"),
    FILE_FORMAT_PARSE_ERROR(1052,"上传文件格式解析出错"),
    ALIYUN_FILE_FORMAT_ERROR(1053,"阿里云转换文件格式出错"),
    ALIYUN_IMG_FORMAT_ERROR(1054,"阿里云图片格式转换出错"),
    
    CHOOSE_DELETE_OBJECT(1061,"请选择要删除的目标"),

    WXWORK_NOT_READY(1071,"企业微信服务没启动"),
    GROUP_HAVE_NO_PARAM(1072,"group接口必须包含group参数"),
    ID_ERROR(1073,"id格式错误"),
    HAVE_NO_WORKWX_FUNCTION(1074,"没有启动企业微信功能"),

    MENU_NAME_REPEAT(1081,"菜单名称已被使用或者该菜单被删除过"),
    MENU_PATH_NEED(1082,"菜单路径必须填写"),

    LOG_SAVE_ERROR(1091,"log保存失败"),
    DATA_SAVE_ERROR(1092,"数据库保存错误"),
    HAVE_NO_TERMINAL(1093,"Header缺少terminal参数"),
    TERMINAL_ERROR(1094,"terminal参数格式不合法"),
    CREATE_NOT_NEED_ID(1095,"创建对象不允许带id"),
    HAVE_NO_ID(1096,"结构中缺少id"),
    POST_DATA_ERROR(1097,"解析post数据异常"),

    HAVE_NO_START_PUBLIC_WEIXIN_FUNCTION(1101,"没有启动微信公众号功能"),
    PBWX_START_ERROR(1102,"微信公众号服务没启动"),
    POST_TO_WEIXIN_ERROR(1103,"post提交给微信错误"),
    USER_WEIXIN_RETURN_JSON_ERROR(1104,"用户微信返回结果json解析失败"),

    ROLE_NAME_CANNOT_REPEAT(1111,"角色名称已被使用"),
    ROLE_CODE_CANNOT_REPEAT(1112,"角色Code已被使用"),
    ROLE_HAVA_USER(1113,"该角色还在使用中，不能删除"),
    ROLE_CODE_CANNOT_UPDATE(1114,"角色CODE不能修改"),
    ROLE_NAME_HAVE_USER_DELETE(1115,"该角色名称被删除过，不能再添加"),
    ROLE_CODE_HAVE_USER_DELETE(1116,"该角色CODE被删除过，不能再添加"),

    USER_NAME_EXIST(1121,"用户名已存在"),
    PASSWORD_NOT_SAME(1122,"两次密码不一样"),
    USER_NOT_EXIST(1123,"用户不存在"),
    UNKNOW_USER_TYPE(1124,"未知用户来源"),
    USER_HAVE_DELETE(1125,"该用户已被删除，不能添加"),

    UNKNOW_PAY_TYPE(1131,"未知充值类型"),
    HAVE_NOT_PAY_AMOUNT(1132,"请输入充值金额"),
    ACCOUNT_NO_MONEY(1133,"账户余额不足"),
    GIVE_ACCOUNT_NO_MONEY(1134,"赠送账户余额不足");


    
    
    
    private Integer code;
    
    private String message;
    
    private ExceptionResultEnum (Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
