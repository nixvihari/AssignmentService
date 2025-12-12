# Assignment Service

### Spark Learning CloudLMS Assignment service handles

- Assignments 
    - Create, view, update, delete assignments
- Submissions
    - Receive submissions, store and grade submissions.

### Assignment Service API Index

#### Assignments

- /api/assignments          [GET] [BASE URL]
- /create                   [POST]
- /{id}                     [GET, DELETE]
- /by-course/{courseId}     [GET]

#### Files

- /{folder}/{filename:.+}


#### Submissions

- /api/submissions                  [GET] [BASE URL]
- /newSubmission                    [POST]
- /{id}                             [GET]
- /by-assignment/{assignmentId}     [GET]
- /{id}                             [DELETE]




