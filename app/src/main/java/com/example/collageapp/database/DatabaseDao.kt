package com.example.collageapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM students")
    suspend fun getAllStudents(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM teachers")
    suspend fun getAllTeachers(): List<Teacher>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher)

    @Query("SELECT * FROM students WHERE login = :login")
    suspend fun getStudentByLogin(login: String): Student?

    @Query("SELECT * FROM teachers WHERE login = :login")
    suspend fun getTeacherByLogin(login: String): Teacher?




    @Transaction
    @Query("SELECT students.*, Speciality.speciality AS studentSpeciality FROM students INNER JOIN Speciality ON students.speciality = Speciality.speciality")
    suspend fun getStudentsWithSpeciality(): List<StudentWithSpeciality>
    @Transaction

    @Query("SELECT students.*, Speciality.speciality AS studentSpeciality FROM students INNER JOIN Speciality ON students.speciality = Speciality.speciality WHERE name = :name")
    suspend fun getStudentsWithSpecialityByName(name:String): List<StudentWithSpeciality>
    @Query("DELETE FROM students")
    suspend fun deleteAllStudents()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<Student>)
    @Query("SELECT * FROM students WHERE name LIKE '%' || :searchName || '%'")
    suspend fun searchStudentsByName(searchName: String): List<Student>
    @Query("SELECT * FROM teachers WHERE name LIKE '%' || :searchName || '%'")
    suspend fun searchTeachersByName(searchName: String): List<Teacher>
    @Query("UPDATE speciality SET speciality = :newSpeciality WHERE speciality = :oldSpeciality")
    suspend fun updateSpecialityName(oldSpeciality: String, newSpeciality: String)
    @Query("UPDATE students SET name = :name, login = :login, password = :password, birthday = :birthday, speciality = :speciality, course = :course, isBudget = :isBudget, photo = :photo WHERE studentId = :studentId")
    suspend fun updateStudent(
        studentId: Long,
        name: String,
        login: String,
        password: String,
        birthday: String,
        speciality: String,
        course: Int,
        isBudget: Boolean,
        photo: String
    )
    @Query("SELECT SUM(hoursPerYear) FROM teachers WHERE name = :teacherName")
    suspend fun getTotalHoursForTeacher(teacherName: String): Int

    @Query("UPDATE teachers SET name = :name, login = :login, password = :password, salary = :salary,hoursPerYear=:hoursPerYear, speciality = :speciality WHERE teacherId = :teacherId")
    suspend fun updateTeacher(
        teacherId: Long,
        name: String,
        login: String,
        password: String,
        salary: Double,
        hoursPerYear:Int,
        speciality: String
    )

    @Delete
    suspend fun deleteStudent(student: Student)
    @Delete
    suspend fun deleteTeacher(teacher:Teacher)
    @Query("Select * from speciality")
    suspend fun getAllSpecialities():List<Speciality>
    @Delete
    suspend fun deleteSpeciality(speciality: Speciality)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeciality(speciality: Speciality)

}
