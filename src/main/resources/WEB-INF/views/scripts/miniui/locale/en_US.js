/**
* jQuery MiniUI v2.0
* 
* Web Site : http://www.miniui.com
*
* Commercial License : http://www.miniui.com/license
*
* Copyright(c) 2012 All Rights Reserved. Shanghai PusSoft Co., Ltd (上海普加软件有限公司) [ services@plusoft.com.cn ]. 
* 
*/


mini.locale = "en-US";


/* Date
-----------------------------------------------------------------------------*/

mini.dateInfo = {
    monthsLong: ["January", "Febraury", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
    monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    daysLong: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
    daysShort: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
    quarterLong: ['Q1', 'Q2', 'Q3', 'Q4'],
    quarterShort: ['Q1', 'Q2', 'Q3', 'Q4'],
    halfYearLong: ['first half', 'second half'],
    patterns: {
        "d": "M/d/yyyy",
        "D": "dddd, MMMM dd, yyyy",
        "f": "dddd, MMMM dd, yyyy H:mm tt",
        "F": "dddd, MMMM dd, yyyy H:mm:ss tt",
        "g": "M/d/yyyy H:mm tt",
        "G": "M/d/yyyy H:mm:ss tt",
        "m": "MMMM dd",
        "o": "yyyy-MM-ddTHH:mm:ss.fff",
        "s": "yyyy-MM-ddTHH:mm:ss",
        "t": "H:mm tt",
        "T": "H:mm:ss tt",
        "U": "dddd, MMMM dd, yyyy HH:mm:ss tt",
        "y": "MMM, yyyy"
    },
    tt: {
        "AM": "AM",
        "PM": "PM"
    },
    ten: {
        "Early": "Early",
        "Mid": "Mid",
        "Late": "Late"
    },
    today: 'Today',
    clockType: 24
};


/* TextBox
-----------------------------------------------------------------------------*/
if (mini.TextBox) {
    mini.copyTo(mini.TextBox.prototype, {
        emailErrorText: "请输入邮件格式",
        urlErrorText: "请输入URL格式",
        floatErrorText: "请输入数字",
        intErrorText: "请输入整数",
        dateErrorText: "请输入日期格式 {0}",
        maxLengthErrorText: "不能超过 {0} 个字符",
        minLengthErrorText: "不能少于 {0} 个字符",
        maxErrorText: "数字不能大于 {0} ",
        minErrorText: "数字不能小于 {0} ",
        rangeLengthErrorText: "字符数必须在 {0} 到 {1} 之间",
        rangeErrorText: "数字必须在 {0} 到 {1} 之间"
    });
}


/* Calendar
-----------------------------------------------------------------------------*/

if (mini.Calendar) {
    mini.copyTo(mini.Calendar.prototype, {
        firstDayOfWeek: 0,
        todayText: "Today",
        clearText: "Clear",
        okText: "OK",
        cancelText: "Cancel",
        daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
        format: "MMM, yyyy",

        timeFormat: 'H:mm'
    });
}


/* TextBox
-----------------------------------------------------------------------------*/
for (var id in mini) {
    var clazz = mini[id];
    if (clazz && clazz.prototype && clazz.prototype.isControl) {
        clazz.prototype.requiredErrorText = "This field is required.";
    }
}
if (mini.TextBox) {
    mini.copyTo(mini.TextBox.prototype, {
        emailErrorText: "Please enter a valid email address.",
        urlErrorText: "Please enter a valid URL.",
        floatErrorText: "Please enter a valid number.",
        intErrorText: "Please enter only digits",
        dateErrorText: "Please enter a valid date. Date format is {0}",
        maxLengthErrorText: "Please enter no more than {0} characters.",
        minLengthErrorText: "Please enter at least {0} characters.",
        maxErrorText: "Please enter a value less than or equal to {0}.",
        minErrorText: "Please enter a value greater than or equal to {0}.",
        rangeLengthErrorText: "Please enter a value between {0} and {1} characters long.",
        rangeErrorText: "Please enter a value between {0} and {1}."
    });
}


/* Pager
-----------------------------------------------------------------------------*/

if (mini.Pager) {
    mini.copyTo(mini.Pager.prototype, {
        firstText: "First",
        prevText: "Prev",
        nextText: "Next",
        lastText: "Last",
        pageSizeText: "Per page："
    });
}

/* Gantt
-----------------------------------------------------------------------------*/
if (window.mini.Gantt) {
    mini.GanttView.ShortWeeks = [
        "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"
    ];
    mini.GanttView.LongWeeks = [
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    ];

    mini.Gantt.PredecessorLinkType = [
        { ID: 0, Name: 'Finish-Finish(FF)', Short: 'FF' },
        { ID: 1, Name: 'Finish-Start(FS)', Short: 'FS' },
        { ID: 2, Name: 'Start-Finish(SF)', Short: 'SF' },
        { ID: 3, Name: 'Start-Start(SS)', Short: 'SS' }
    ];

    mini.Gantt.ConstraintType = [
        { ID: 0, Name: 'The sooner the better' },
        { ID: 1, Name: 'The later the better' },
        { ID: 2, Name: 'Must be begin in' },
        { ID: 3, Name: 'Must be completed in' },
        { ID: 4, Name: 'Beginning no earlier than ...' },
        { ID: 5, Name: 'Beginning no later than ...' },
        { ID: 6, Name: 'Completed no earlier than ...' },
        { ID: 7, Name: 'Completed no later than ...' }
    ];

    mini.copyTo(mini.Gantt, {
        ID_Text: "ID",
        Name_Text: "Name",
        PercentComplete_Text: "Progress",
        Duration_Text: "Duration",
        Start_Text: "Start",
        Finish_Text: "Finish",
        Critical_Text: "Critical",

        PredecessorLink_Text: "PredecessorLink",
        Work_Text: "Work",
        Priority_Text: "Priority",
        Weight_Text: "Weight",
        OutlineNumber_Text: "OutlineNumber",
        OutlineLevel_Text: "OutlineLevel",
        ActualStart_Text: "ActualStart",
        ActualFinish_Text: "ActualFinish",
        WBS_Text: "WBS",
        ConstraintType_Text: "ConstraintType",
        ConstraintDate_Text: "ConstraintDate",
        Department_Text: "Department",
        Principal_Text: "Principal",
        Assignments_Text: "Assignments",

        Summary_Text: "Summary",
        Task_Text: "Task",
        Baseline_Text: "Baseline",
        LinkType_Text: "LinkType",
        LinkLag_Text: "LinkLag",
        From_Text: "From",
        To_Text: "To",

        Goto_Text: "Goto",
        UpGrade_Text: "UpGrade",
        DownGrade_Text: "DownGrade",
        Add_Text: "Add Task",
        Edit_Text: "Edit Task",
        Remove_Text: "Remove Task",
        Move_Text: "Move Task",
        ZoomIn_Text: "ZoomIn",
        ZoomOut_Text: "ZoomOut",
        Deselect_Text: "Un Select",
        Split_Text: "Split Task"


    });
}