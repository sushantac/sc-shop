

export class User {
   
    public id: string;

    constructor(
         public username: string,
         public password: string,
         public firstName: string = "FirstName",
         public lastName: string = "LastName",
         public birthDate: Date = new Date(),
         public gender: string = "Male",
         public credits: number = 100000
        ) {

    }

}




