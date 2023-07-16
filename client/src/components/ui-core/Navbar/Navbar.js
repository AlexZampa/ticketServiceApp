/*
 * --------------------------------------------------------------------
 *
 * Package:         client
 * Module:          src/components/ui-core/Navbar
 * File:            Navbar.js
 *
 * --------------------------------------------------------------------
 */

//Imports
import 'bootstrap/dist/css/bootstrap.min.css';
import {Button, Navbar as MyNavbar} from "react-bootstrap";
import {PersonCircle, Ticket} from 'react-bootstrap-icons';
import {useContext} from "react";
import {AuthContext} from "../../utils/AuthContext";

const Navbar = () => {
    const authContext = useContext(AuthContext);

    return (
        <MyNavbar expand="lg" variant="dark" fixed="top" className='bg-black w-100'>
            <MyNavbar.Brand href="/" className='ms-2'>
                <Ticket/>
                <span className="ms-2"> Ticket Service</span>
            </MyNavbar.Brand>
            <MyNavbar.Brand href="profile" className='justify-content-end logout-text ms-lg-auto'>
                <PersonCircle className='person-circle' color='white' size={25}/>
                {
                    authContext.user.token ?
                        <Button size='sm' className="ms-2" variant={'secondary '}>
                            {authContext.user.username}
                        </Button>
                        :
                        <Button size='sm' className="ms-2" variant={'secondary '}>
                            login
                        </Button>
                }
            </MyNavbar.Brand>
        </MyNavbar>
    );
};

export default Navbar;
