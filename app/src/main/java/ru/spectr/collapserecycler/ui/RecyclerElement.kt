package ru.spectr.collapserecycler.ui

sealed class RecyclerElement(val name: String, open var isChecked: Boolean = false)


class Group(name: String, var isExpanded: Boolean, val subGroups: List<SubGroup>) :
    RecyclerElement(name) {
    fun setAllChecked(checked: Boolean) {
        isChecked = checked
        subGroups.map {
            it.isChecked = checked
            it.setAllChecked(checked)
        }
    }

    fun expandableElements(): Int {
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

class SubGroup(name: String, var isExpanded: Boolean, val contacts: MutableList<Contact>) :
    RecyclerElement(name) {
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

class Contact(name: String, val phone: String) : RecyclerElement(name)
