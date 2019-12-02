package ru.spectr.collapserecycler.ui

import ru.spectr.collapserecycler.generateTestData

class MainActivityPresenter {
    private lateinit var view: MainActivityView
    private val groups: List<Group> = generateTestData()

    private var nameSortByDesc = false
    private var phoneSortByDesc = false

    fun attach(view: MainActivityView) {
        this.view = view
        view.setContacts(groups)
    }

    fun onContactNameClick() {
        nameSortByDesc = !nameSortByDesc
        sortByName()
        view.setContacts(groups)
        view.setNameSortState(nameSortByDesc)
    }

    fun onContactPhoneClick() {
        phoneSortByDesc = !phoneSortByDesc
        sortByPhone()
        view.setContacts(groups)
        view.setPhoneSortState(phoneSortByDesc)
    }

    fun onSelectAllClick() {
        val allSelected = isAllSelected()
        for (group in groups)
            group.setAllChecked(!allSelected)
        view.setAllSelected(isAllSelected())
        view.updateContacts()
        view.showNextButton(isSomethingSelected())
    }

    fun onContactSelected(contact: Contact) {
        val subGroup = getSubGroupByContact(contact)!!
        if (subGroup.isChecked != subGroup.isAllChecked()) {
            subGroup.isChecked = subGroup.isAllChecked()
            view.updateElement(subGroup)
            onSubGroupSelected(subGroup)
        } else
            onSomethingSelected()
    }

    fun onSubGroupSelected(subGroup: SubGroup) {
        val group = getGroupBySubgroup(subGroup)
        if (group.isChecked != group.isAllChecked()) {
            group.isChecked = group.isAllChecked()
            view.updateElement(group)
            onGroupSelected(group)
        } else
            onSomethingSelected()
    }

    fun onGroupSelected(group: Group) {
        onSomethingSelected()
    }

    private fun onSomethingSelected() {
        view.setAllSelected(isAllSelected())
        view.showNextButton(isSomethingSelected())
    }

    private fun getGroupBySubgroup(subGroup: SubGroup): Group {
        return groups.find { it.subGroups.contains(subGroup) }!!
    }

    private fun getSubGroupByContact(contact: Contact): SubGroup? {
        for (group in groups)
            for (subGroup in group.subGroups)
                if (subGroup.contacts.contains(contact))
                    return subGroup
        return null
    }

    private fun sortByName() {
        for (group in groups)
            for (subgroup in group.subGroups)
                if (nameSortByDesc)
                    subgroup.contacts.sortByDescending { it.name }
                else
                    subgroup.contacts.sortBy { it.name }
    }

    private fun sortByPhone() {
        for (group in groups)
            for (subgroup in group.subGroups)
                if (phoneSortByDesc)
                    subgroup.contacts.sortByDescending { it.phone }
                else
                    subgroup.contacts.sortBy { it.phone }
    }

    private fun isAllSelected(): Boolean {
        for (group in groups)
            if (!group.isAllChecked())
                return false
        return true
    }

    private fun isSomethingSelected(): Boolean {
        for (group in groups)
            for (subgroup in group.subGroups)
                for (contact in subgroup.contacts)
                    if (contact.isChecked)
                        return true
        return false
    }

}