package com.zhongou.utils;

public class WebUrl {
    /**
     * 根接口
     */
    // 云端
    private static final String LOGIN_URL = "http://59.110.26.83:8086/";


    /**
     * 管理根目录
     */
    public static final String LOGIN_FLODER_USER = "openapi/";

    /**
     * 使用者管理
     *
     * @author JackSong
     */
    public class UserManager {


        /**
         * 01 密码登录
         */
        public static final String LOGIN_POST = LOGIN_URL + LOGIN_FLODER_USER + "User/LoginByPassword";


        /**
         * 02 修改登录密码
         * http://192.168.1.127:9012/openapi/User/ChangePasswordN
         */
        public static final String CHANGE_PASSWORD = LOGIN_URL + LOGIN_FLODER_USER + "Password/UpdatePassword";

    }

    public class AppsManager {


        /**
         * 01-01获取 我的申请 记录
         */
        public static final String GETMYAPPLICATIONRECORD = LOGIN_URL + LOGIN_FLODER_USER + "ApprovalInfo/GetMyApplicationSearchResults";
        /**
         * 01-01-获取 申请详情
         */
        public static final String APPLICATIONDETAIL = LOGIN_URL + LOGIN_FLODER_USER + "AllApplicationInfo/GetAllApplicationInfoByAppID";


        /**
         * 01-02获取 我的审批 记录
         */
        public static final String GETMYAPPROVALRECORD = LOGIN_URL + LOGIN_FLODER_USER + "Approval/GetApprovalSearchResults";

        /**
         * 01-02获取 审批详情 记录
         */
        public static final String GETMYAPPROVALDETAIL = LOGIN_URL + LOGIN_FLODER_USER + "AllApprovalInfo/GetAllApprovalInfoByAppID";

        /**
         * 01-02-01/02 审批 同意/驳回
         */
        public static final String APPROVALE_AGREE_DISAGREE = LOGIN_URL + LOGIN_FLODER_USER + "Approval/ApprovalMyAppRecord";

        /**
         * 01-02-03审批 转发
         */
        public static final String APPROVAL_TRANSFORTO = LOGIN_URL + LOGIN_FLODER_USER + "Approval/TurnMyApprovalRecord";

        /**
         * 01-02-04审批 抄送
         */
        public static final String APPROVAL_COPYTO = LOGIN_URL + LOGIN_FLODER_USER + "Approval/CopyMyApprovalRecord";


        /**
         * 01-03获取 我的抄送 记录
         */
        public static final String GETCOPYLIST = LOGIN_URL + LOGIN_FLODER_USER + "Mycopy/MycopyList";
        /**
         * 01-03获取 抄送详情
         */
        public static final String GETCOPYDETAIL = LOGIN_URL + LOGIN_FLODER_USER + "AllCopyListInfo/GetAllCopyListInfo";


        /**
         * 01-04-01 招聘申请
         */
        public static final String RECRUITMENTPOST = LOGIN_URL + LOGIN_FLODER_USER + "Recruitment/RecruitmentPost";
        /**
         * 01-04-02 离职申请
         */
        public static final String DIMISSIONPOST = LOGIN_URL + LOGIN_FLODER_USER + "Dimission/DimissionPost";

        /**
         * 01-04-03 请假申请
         */
        public static final String LEAVEPOST = LOGIN_URL + LOGIN_FLODER_USER + "Leave/LeavePost";

        /**
         * 01-04-04 加班申请
         */
        public static final String OVERAPPROVALPOST = LOGIN_URL + LOGIN_FLODER_USER + "OverApproval/OverApprovalPost";
        /**
         * 01-04-05 调休申请
         */
        public static final String TAKEDAYSOFFPOST = LOGIN_URL + LOGIN_FLODER_USER + "TakeDaysOff/TakeDaysOffPost";

        /*
         * 01-04-06 借阅申请
         */
        public static final String BORROWPOST = LOGIN_URL + LOGIN_FLODER_USER + "Borrow/BorrowPost";

        /*
         * 01-04-07 调薪申请
         */
        public static final String CHANGESALARY = LOGIN_URL + LOGIN_FLODER_USER + "ChangeSalary/ChangeSalaryPost";

        /*
         * 01-04-08 用车申请
         */
        public static final String VEHICLEPOST = LOGIN_URL + LOGIN_FLODER_USER + "Vehicle/VehiclePost";

        /*
         * 01-04-09 车辆维护申请
         */
        public static final String MAINTENANCE = LOGIN_URL + LOGIN_FLODER_USER + "Maintenance/MaintenancePost";

        /*
         * 01-04-10 报销申请
         */
        public static final String LRAPPLICATIONPOST = LOGIN_URL + LOGIN_FLODER_USER + "LRApplication/LRApplicationPost";

        /**
         * 09-01交车记录
         */
        public static final String VEHICLERETURNLIST = LOGIN_URL + LOGIN_FLODER_USER + "MyBackcar/MyBackcarList";

        /**
         * 09-02交车-详情接口
         */
        public static final String VEHICLERETURNDETAIL = LOGIN_URL + LOGIN_FLODER_USER + "MyBack/MyBackInfo";
        /**
         * 09-03交车-提交接口
         */
        public static final String VEHICLERETURNDPOST = LOGIN_URL + LOGIN_FLODER_USER + "BackCar/BackCarPost";

        /**
         * 03地图签到
         */
        public static final String ATTENDRECORD = LOGIN_URL + LOGIN_FLODER_USER + "Attence/AttenceRecord";

        /**
         * 03-02获取地图考勤记录
         */
        public static final String GETATTENDRECORD = LOGIN_URL + LOGIN_FLODER_USER + "AttenceRecord/GetAttenceRecord";
    }

    /**
     * 通讯录接口
     */
    public class ContactsManager {
        /*
        *  获取整个公司所有分公司员工信息接口
        */
        public static final String GETCOMPANYSONOFCO = LOGIN_URL + LOGIN_FLODER_USER + "Contacts/GetStoreInfoByStoreId";

        /*
          *  获取分公司所有部门员工信息接口
          */
        public static final String DEPTINFOBYSTOREID = LOGIN_URL + LOGIN_FLODER_USER + "Contacts/GetDeptInfoByStoreId";

        /*
          *  获取分公司所有部门员工信息接口
          */
        public static final String EMPLOYEEINFOBYDEPTID = LOGIN_URL + LOGIN_FLODER_USER + "Contacts/GetEmployeeInfoByDeptId";

        /*
          * 选择审批人
          */
        public static final String CONTACTSSELECTCO = LOGIN_URL + LOGIN_FLODER_USER + "GetApplication/GetApplicationByThisEmp";

    }

}
