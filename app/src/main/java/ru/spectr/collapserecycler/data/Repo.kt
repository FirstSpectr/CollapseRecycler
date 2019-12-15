package ru.spectr.collapserecycler.data

import ru.spectr.collapserecycler.data.entity.Contact
import ru.spectr.collapserecycler.data.entity.Group
import ru.spectr.collapserecycler.generateNetworkData

class Repo {
    fun getGroups(): List<ru.spectr.collapserecycler.ui.Group> {
        return mapToViewModel(generateNetworkData())
    }

    private fun mapToViewModel(networkGroups: List<Group>): List<ru.spectr.collapserecycler.ui.Group> {
        val groups = mutableListOf<ru.spectr.collapserecycler.ui.Group>()
        for (group in networkGroups) {
            val subGroups = mutableListOf<ru.spectr.collapserecycler.ui.SubGroup>()
            for (subgroup in group.subGroups) {
                val contacts = mutableListOf<ru.spectr.collapserecycler.ui.Contact>()
                for (contact in subgroup.contacts) {
                    contacts.add(
                        ru.spectr.collapserecycler.ui.Contact(
                            contact.id,
                            contact.userName,
                            contact.userPhone
                        )
                    )
                }
                subGroups.add(
                    ru.spectr.collapserecycler.ui.SubGroup(
                        subgroup.id,
                        subgroup.name,
                        contacts
                    )
                )
            }
            groups.add(ru.spectr.collapserecycler.ui.Group(group.id, group.name, subGroups))
        }
        return groups
    }

    fun saveContacts(uiContacts: List<ru.spectr.collapserecycler.ui.Contact>) {
        val networkContacts = mutableListOf<Contact>()
        for (contact in uiContacts)
            networkContacts.add(Contact(contact.id, contact.name, contact.phone))

        //TODO: Api.saveContacts(networkContacts)
    }
}