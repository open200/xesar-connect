package com.open200.xesar.connect.messages.command

enum class FilterType(val filterString: String? = null) {
    EQ("eq"),
    GT("gt"),
    LT("lt"),
    GTE("gte"),
    LTE("lte"),
    CONTAINS("contains"),
    EMPTY("empty"),
    NE("ne"),
    IS_NULL("isNull"),
    IS_NOT_NULL("isNotNull"),
    LIKE("like"),
    BETWEEN("between"),
    CUSTOM("custom")
}
