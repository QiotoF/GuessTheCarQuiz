package com.pericle.guessthecar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Car::class, Level::class], version = 3, exportSchema = false)
@TypeConverters(MyConverters::class)
abstract class MyDatabase : RoomDatabase() {

    /**
     * Connects the carDao to the DAO.
     */
    abstract val carDao: CarDao
    abstract val levelDao: LevelDao

    /**
     * Define a companion object, this allows us to add functions on the SleepDatabase class.
     *
     * For example, clients can call `SleepDatabase.getInstance(context)` to instantiate
     * a new SleepDatabase.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any carDao returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the carDao, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: MyDatabase? = null

        /**
         * Helper function to get the carDao.
         *
         * If a carDao has already been retrieved, the previous carDao will be returned.
         * Otherwise, create a new carDao.
         *
         * This function is threadsafe, and callers should cache the result for multiple carDao
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): MyDatabase {
            // Multiple threads can ask for the carDao at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new carDao instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "car_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created carDao.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}
