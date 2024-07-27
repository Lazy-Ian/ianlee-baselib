package palm.catering.kds.network.params

/**
 * Created by Ian on 2024/4/1
 * Email: yixin0212@qq.com
 * Function : *
 * {"id": 1,"name": "点餐端H5"},
 *  {"id": 2,"name": "点餐端APP"},
 *  {"id": 3,"name": "收银端H5"},
 *  {"id": 4,"name": "收银端APP"},
 *  {"id": 5,"name": "后厨端APP"},
 *  {"id": 6,"name": "点餐大屏APP"},
 *  {"id": 7,"name": "叫号大屏APP"}
 *
 *    {"id": 1,"name": "云餐"},
 *    {"id": 2,"name": "云商"},
 *    {"id": 3,"name": "通用"}
 *
 */
data class MultilingualInfoParams(
    var line: MutableList<LangLine> = arrayListOf(),
    var client: ProgramInfo = ProgramInfo("4", "收银端APP"),
    var program: ProgramInfo = ProgramInfo("1", "云餐")
) {
    data class LangLine(
        var index: String = "",   // update1m_s_tarde
        var chinese: String = "",  // 现在更新
        var english: String = "",  // User login
        var spanish: String = "", // Más tarde
        var catalan: String = "",  // Más tarde
        var french: String = "",  //

    )

    data class ProgramInfo(
        var id: String = "",
        var name: String = "",
    )
}
