import { createContext } from "react";

export const AuthContext = createContext({
    user: { id: undefined, email: undefined, token: undefined, username: undefined, name: undefined, surname: undefined, dateOfBirth: undefined },
    setUser: (user) => {},
    resetUser: () => {}
});
