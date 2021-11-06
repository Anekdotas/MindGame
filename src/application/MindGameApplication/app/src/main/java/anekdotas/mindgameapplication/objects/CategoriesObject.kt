package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.network.CategoryModel

object CategoriesObject {
    var categoryList = listOf(CategoryModel(0, "No Internet"))
    var selectedCategory = categoryList[0]
}