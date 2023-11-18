CREATE TABLE "Courses"(
    "course_id" TEXT,
    "course_level" INTEGER CHECK ("course_level" BETWEEN 1 AND 4),
    "course_name" TEXT NOT NULL,
    "course_description" TEXT,
    "max_degree" INTEGER NOT NULL CHECK ("max_degree" > 0) DEF,
    "min_degree" INTEGER NOT NULL CHECK ("min_degree" >=0 ),
    "success_degree" INTEGER NOT NULL CHECK ("success_degree" >= "min_degree" AND "success_degree" <= "max_degree"),
    PRIMARY KEY ("course_id")
);

CREATE TABLE "Students"(
    "username"  TEXT,
    "password" TEXT NOT NULL,
    "level" INTEGER NOT NULL,
    "first_name" TEXT NOT NULL,
    "last_name" TEXT NOT NULL,
    "phone_number" TEXT NOT NULL,
    "age" INTEGER NOT NULL,
    check ("age" > 0),
    check ("level" BETWEEN 1 AND 4),
    PRIMARY KEY ("username")
);

CREATE TABLE "Student_Courses"(
    "username" TEXT,
    "course_id" TEXT,
    "grade" INTEGER NOT NULL,
    FOREIGN KEY("username") REFERENCES "Students"("username"),
    FOREIGN KEY("course_id") REFERENCES "Courses"("course_id")
);