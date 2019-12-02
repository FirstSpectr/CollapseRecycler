package ru.spectr.collapserecycler

import ru.spectr.collapserecycler.ui.Contact
import ru.spectr.collapserecycler.ui.Group
import ru.spectr.collapserecycler.ui.SubGroup
import kotlin.random.Random

fun generateNumber(): String {
    var result = "+"
    result += Random.nextInt(1, 9)
    result += " "
    result += Random.nextInt(100, 999)
    result += " "
    result += Random.nextInt(100, 999)
    result += " "
    result += Random.nextInt(10, 99)
    result += " "
    result += Random.nextInt(10, 99)
    return result
}

val vowels = arrayOf("A", "E", "I", "O", "U", "Y")
val consonants = arrayOf(
    "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P",
    "Q", "R", "S", "T", "V", "W", "X", "Y", "Z"
)

fun generateName(): String {
    val n = Random.nextInt(3, 10)
    val b = if(Random.nextBoolean()) 0 else 1
    var result = ""
    for (i in 0..n)
        result += if (i % 2 == b)
            consonants.random()
        else
            vowels.random()
    return result
}

fun generateTestData(): List<Group> {
    val n1 = 2
    val n2 = 2
    val n3 = 5
    val groups = ArrayList<Group>()
    for (i in 0 until n1) {
        val subGroups = ArrayList<SubGroup>()
        for (j in 0 until n2) {
            val contacts = ArrayList<Contact>()
            for (k in 0 until n3)
                contacts.add(Contact(generateName(), generateNumber()))
            subGroups.add(SubGroup("SUBGROUP $i - $j", true, contacts))
        }
        groups.add(Group("GROUP $i", true, subGroups))
    }
    return groups
}