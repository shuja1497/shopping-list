package dev.zenolabs.groceryshopping.data.local

enum class Category(val displayName: String, val icon: String) {
    MILK("Milk", "\uD83E\uDD5B"),
    VEGETABLES("Vegetables", "\uD83E\uDD66"),
    FRUITS("Fruits", "\uD83C\uDF4E"),
    BREADS("Breads", "\uD83C\uDF5E"),
    MEATS("Meats", "\uD83E\uDD69");

    companion object {
        fun fromName(name: String): Category =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: MILK
    }
}
