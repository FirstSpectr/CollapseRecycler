package ru.spectr.collapserecycler.ui

import android.util.Log
import ru.spectr.collapserecycler.data.Repo

class MainActivityPresenter {
    //Todo inject
    private val repo = Repo()

    private lateinit var view: MainActivityView
    private lateinit var groups: List<Group>

    private var nameSortByDesc = false
    private var phoneSortByDesc = false

    fun attach(view: MainActivityView) {
        this.view = view
        groups = repo.getGroups()
        view.setContacts(groups)
    }

    private fun getSelected(): List<Contact> {
        val selectedContacts = mutableListOf<Contact>()
        for (group in groups) {
            for (subgroup in group.subGroups)
                for (contact in subgroup.contacts)
                    if (contact.isChecked)
                        selectedContacts.add(contact)
        }
        return selectedContacts
    }

    fun onNextButtonClick() {
        repo.saveContacts(getSelected())
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
        Log.d("MY_TAG", "GROUP SELECTED ${group.name}")
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