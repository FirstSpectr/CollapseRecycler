package ru.spectr.collapserecycler.ui

sealed class RecyclerElement(val id: Int, val name: String, open var isChecked: Boolean = false)


class Group(id: Int, name: String, val subGroups: List<SubGroup>, var isExpanded: Boolean = true) :
    RecyclerElement(id, name) {
    fun setAllChecked(checked: Boolean) {
        isChecked = checked
        subGroups.map {
            it.isChecked = checked
            it.setAllChecked(checked)
        }
    }

    fun expandableElementsCount(): Int {
        var count = subGroups.size
        for (subGroup in subGroups)
            if (subGroup.isExpanded)
                count += subGroup.contacts.size
        return count
    }

    fun isAllChecked(): Boolean {
        for (subGroup in subGroups)
            if (!subGroup.isAllChecked())
                return false
        return true
    }
}

class SubGroup(
    id: Int,
    name: String,
    val contacts: MutableList<Contact>,
    var isExpanded: Boolean = true
) :
    RecyclerElement(id, name) {
    fun setAllChecked(checked: Boolean) {
        isChecked = checked
        contacts.map { it.isChecked = checked }
    }

    fun isAllChecked(): Boolean {
        for (contact in contacts)
            if (!contact.isChecked)
                return false
        return true
    }
}

class Contact(id: Int, name: String, val phone: String) : RecyclerElement(id, name)
