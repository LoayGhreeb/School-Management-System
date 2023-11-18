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

