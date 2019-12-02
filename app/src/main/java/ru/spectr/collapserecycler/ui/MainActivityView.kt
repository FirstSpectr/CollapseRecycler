package ru.spectr.collapserecycler.ui

interface MainActivityView {
    fun setContacts(groups: List<Group>)
    fun setAllSelected(checked: Boolean)
    fun updateContacts()
    fun setNameSortState(descending: Boolean)
    fun setPhoneSortState(descending: Boolean)
    fun showNextButton(show: Boolean)
    fun updateElement(element: RecyclerElement)
}