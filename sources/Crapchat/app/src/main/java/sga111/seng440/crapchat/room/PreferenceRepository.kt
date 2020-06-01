package sga111.seng440.crapchat.room

class PreferenceRepository(private val preferenceDao: PreferenceDao) {

    fun insert(preference: Preference) {
        preferenceDao.insert(preference)
    }

    fun retrieve(key: String) : Preference {
        return preferenceDao.retrieve(key)
    }

}