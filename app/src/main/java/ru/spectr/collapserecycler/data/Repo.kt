package ru.spectr.collapserecycler.data

import ru.spectr.collapserecycler.data.entity.Group
import ru.spectr.collapserecycler.data.entity.SubGroup
import ru.spectr.collapserecycler.data.entity.Contact
import kotlin.random.Random

class Repo {
    private val N = 3
    fun getGroups(): List<Group> {
        return generateData()
    }

    private fun generateData(): List<Group>{
        val random = Random(System.currentTimeMillis())
        val groups = ArrayList<Group>()
        for (i in 0..N) {
            val subGroups = ArrayList<SubGroup>()
            for (j in 0..N) {
                val users = ArrayList<Contact>()
                for (k in 0..N)
                    users.add(Contact(random.nextInt(),"$i - $j - $k", k.toString()))
                subGroups.add(SubGroup(random.nextInt(), "$i - $j", users))
            }
            groups.add(Group(random.nextInt(), "$i", subGroups))
        }
        return groups
    }
}