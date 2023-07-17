import { createContext } from "react";

export const AuthContext = createContext({
    user: { id: undefined, email: undefined, token: undefined, username: undefined, name: undefined, surname: undefined, dateOfBirth: undefined, role: undefined },
    setUser: (user) => {
        return(
            {
                id: user.id,
                email: user.email,
                token: user.token,
                username: user.username,
                name: user.name,
                surname: user.surname,
                dateOfBirth: user.dateOfBirth,
                role: user.role
            }
        )
    },
    resetUser: () => {}
});
