package com.buit.commons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * 类名称：SysUser<br>
 * 类描述：系统用户表<br>
 *
 * @author All
 */
@ApiModel(value = "系统用户表")
public class SysUser extends PageQuery {
    @ApiModelProperty(value = "用户数据ID")
    /** 用户数据ID */
    private Integer userId;
    @ApiModelProperty(value = "医疗机构ID")
    /** 医疗机构ID */
    private Integer hospitalId;
    /** 医疗机构名称 */
    private String hospitalName;
    /**
     * 数据版本
     */
    private Integer dataVersion;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @ApiModelProperty(value = "数据创建时间")
    /** 数据创建时间 */
    private Timestamp gmtCreate;
    @ApiModelProperty(value = "数据修改时间")
    /** 数据修改时间 */
    private Timestamp gmtModify;
    @ApiModelProperty(value = "登录账号")
    /** 系统用户登录名 */
    private String loginName;
    @ApiModelProperty(value = "卫生服务人员ID")
    /** 卫生服务人员ID */
    private Integer personId;
    @ApiModelProperty(value = "登录密码")
    /** 登录密码 */
    private String userPwd;
    @ApiModelProperty(value = "用户姓名")
    /** 用户姓名 */
    private String userName;
    @ApiModelProperty(value = "科室ID  字典:sys_dict_config:4-科室字典  门诊科室")
    private Integer deptId;
    //    @ApiModelProperty(value="业务权限代码  挂号科室")
//    private Integer ksdm;
    @ApiModelProperty(value = "锁定标识")
    /** 锁定标识 */
    private String lockFlag;
    @ApiModelProperty(value = "用户类型标识")
    /** 用户类型标识 */
    private String userTypes;
    @ApiModelProperty(value = "失效日期")
    /** 失效日期 */
    private Timestamp endDate;
    @ApiModelProperty(value = "在线标识")
    /** 在线标识 */
    private String userLine;
    @ApiModelProperty(value = "图像文件名")
    /** 图像文件名 */
    private String image;
    @ApiModelProperty(value = "拼音码")
    /** 拼音码 */
    private String pyCode;
    @ApiModelProperty(value = "五笔码")
    /** 五笔码 */
    private String wbCode;
    @ApiModelProperty(value = "最后登录时间")
    /** 最后登录时间 */
    private Timestamp lastLoginTime;
    @ApiModelProperty(value = "累计登录次数")
    /** 累计登录次数 */
    private Integer loginCount;
    //@ApiModelProperty(value="引导页列表 json 页面+是否不再引导 举例:{'门诊诊疗': '0','医嘱录入': '0'}，名称后的0表示需要引导，1表示不需要")
    /**
     * 引导页列表 json 页面+是否不再引导 举例:{'门诊诊疗': '0','医嘱录入': '0'}，名称后的0表示需要引导，1表示不需要
     */
    private String guidePageList;
    /**
     * 用户组数据ID
     */
    private Integer groupId;
    @ApiModelProperty(value = "是否强制修改密码:1:强制修改,2:不强制")
    private Integer changPasswd;
    @ApiModelProperty(value = "工作证号")
    private String empNo;
    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIp;
    @ApiModelProperty(value = "角色序号")
    private Integer jsxh;
    @ApiModelProperty(value = "角色级别")
    private Integer jsjb;
    @ApiModelProperty(value = "上次密码修改时间")
    private Timestamp lastChangePwdTime;
    @ApiModelProperty("医生签名图片路径")
    private String ysqm;
    @ApiModelProperty("密码登录错误超过10次锁定标识  0锁定 1正常")
    private Integer loginErrorFlag;

    public Integer getLoginErrorFlag() {
        return loginErrorFlag;
    }

    public void setLoginErrorFlag(Integer loginErrorFlag) {
        this.loginErrorFlag = loginErrorFlag;
    }

    public Integer getChangPasswd() {
        return changPasswd;
    }

    public void setChangPasswd(Integer changPasswd) {
        this.changPasswd = changPasswd;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Timestamp gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 设置:用户数据ID
     */
    public void setUserId(Integer value) {
        this.userId = value;
    }

    /**
     * 获取:用户数据ID
     */
    public Integer getUserId() {
        return userId;
    }

    public String getUserIdStr() {
        if (userId != null) {
            return userId.toString();
        }
        return null;
    }

    /**
     * 设置:医疗机构ID
     */
    public void setHospitalId(Integer value) {
        this.hospitalId = value;
    }

    /**
     * 获取:医疗机构ID
     */
    public Integer getHospitalId() {
        return hospitalId;
    }

    public String getHospitalIdStr() {
        if (hospitalId != null) {
            return hospitalId.toString();
        }
        return null;
    }

    /**
     * 设置:系统用户登录名
     */
    public void setLoginName(String value) {
        this.loginName = value;
    }

    /**
     * 获取:系统用户登录名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置:卫生服务人员ID
     */
    public void setPersonId(Integer value) {
        this.personId = value;
    }

    /**
     * 获取:卫生服务人员ID
     */
    public Integer getPersonId() {
        return personId;
    }

    /**
     * 设置:登录密码
     */
    public void setUserPwd(String value) {
        this.userPwd = value;
    }

    /**
     * 获取:登录密码
     */
    public String getUserPwd() {
        return userPwd;
    }

    /**
     * 设置:用户姓名
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * 获取:用户姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置:科室ID
     */
    public void setDeptId(Integer value) {
        this.deptId = value;
    }

    /**
     * 获取:科室ID
     */
    public Integer getDeptId() {
        return deptId;
    }

    /**
     * 设置:锁定标识
     */
    public void setLockFlag(String value) {
        this.lockFlag = value;
    }

    /**
     * 获取:锁定标识
     */
    public String getLockFlag() {
        return lockFlag;
    }

    /**
     * 设置:用户类型标识
     */
    public void setUserTypes(String value) {
        this.userTypes = value;
    }

    /**
     * 获取:用户类型标识
     */
    public String getUserTypes() {
        return userTypes;
    }

    /**
     * 设置:失效日期
     */
    public void setEndDate(Timestamp value) {
        this.endDate = value;
    }

    /**
     * 获取:失效日期
     */
    public Timestamp getEndDate() {
        return endDate;
    }

    /**
     * 设置:在线标识
     */
    public void setUserLine(String value) {
        this.userLine = value;
    }

    /**
     * 获取:在线标识
     */
    public String getUserLine() {
        return userLine;
    }

    /**
     * 设置:图像文件名
     */
    public void setImage(String value) {
        this.image = value;
    }

    /**
     * 获取:图像文件名
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置:拼音码
     */
    public void setPyCode(String value) {
        this.pyCode = value;
    }

    /**
     * 获取:拼音码
     */
    public String getPyCode() {
        return pyCode;
    }

    /**
     * 设置:五笔码
     */
    public void setWbCode(String value) {
        this.wbCode = value;
    }

    /**
     * 获取:五笔码
     */
    public String getWbCode() {
        return wbCode;
    }

    /**
     * 设置:最后登录时间
     */
    public void setLastLoginTime(Timestamp value) {
        this.lastLoginTime = value;
    }

    /**
     * 获取:最后登录时间
     */
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置:累计登录次数
     */
    public void setLoginCount(Integer value) {
        this.loginCount = value;
    }

    /**
     * 获取:累计登录次数
     */
    public Integer getLoginCount() {
        return loginCount;
    }

    /**
     * 设置:引导页列表 json 页面+是否不再引导 举例:{'门诊诊疗': '0','医嘱录入': '0'}，名称后的0表示需要引导，1表示不需要
     */
    public void setGuidePageList(String value) {
        this.guidePageList = value;
    }

    /**
     * 获取:引导页列表 json 页面+是否不再引导 举例:{'门诊诊疗': '0','医嘱录入': '0'}，名称后的0表示需要引导，1表示不需要
     */
    public String getGuidePageList() {
        return guidePageList;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getJsxh() {
        return jsxh;
    }

    public void setJsxh(Integer jsxh) {
        this.jsxh = jsxh;
    }

    public Integer getJsjb() {
        return jsjb;
    }

    public void setJsjb(Integer jsjb) {
        this.jsjb = jsjb;
    }

    public Timestamp getLastChangePwdTime() {
        return lastChangePwdTime;
    }

    public void setLastChangePwdTime(Timestamp lastChangePwdTime) {
        this.lastChangePwdTime = lastChangePwdTime;
    }

    public String getYsqm() {
        return ysqm;
    }

    public void setYsqm(String ysqm) {
        this.ysqm = ysqm;
    }
}