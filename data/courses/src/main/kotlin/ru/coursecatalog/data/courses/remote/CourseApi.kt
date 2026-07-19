package ru.coursecatalog.data.courses.remote

import retrofit2.http.GET
import retrofit2.http.Query

internal interface CourseApi {
    @GET("u/0/uc")
    suspend fun getCourses(
        @Query("id") fileId: String = COURSES_FILE_ID,
        @Query("export") export: String = "download",
    ): CoursesResponse

    private companion object {
        const val COURSES_FILE_ID = "15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q"
    }
}
