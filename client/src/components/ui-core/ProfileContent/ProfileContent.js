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
import {Button } from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import {BsPencil} from "react-icons/bs";
import {useContext, useEffect} from "react";
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import useNotification from "../../utils/useNotification";
import {AuthContext} from "../../utils/AuthContext";
import API from "../../../services/Api";

const ProfileContent = () => {
    const authContext = useContext(AuthContext);
    const notify = useNotification()
    const navigate = useNavigate();

    useEffect(() => {
        API.getProfileByEmail(authContext.user.email, authContext.user.token)
            .then((user) => {
                authContext.setUser({
                    id: user.id,
                    email: authContext.user.email,
                    token: authContext.user.token,
                    username: user.username,
                    name: user.name,
                    surname: user.surname,
                    dateOfBirth: user.dateOfBirth,
                    role: user.role
                });
            })
            .catch(err => {
                authContext.resetUser()
                navigate('/login')
                //notify.error(err.title ? err.title.toString() : "Session Error");
            })
        // eslint-disable-next-line
    }, []);

    const handleLogout = (event) => {
        event.preventDefault();
        API.logout(authContext.user.token).then((succes) => {
            authContext.resetUser()
            notify.success(succes.title ? succes.title.toString() : "Successfully logout!");
            navigate('/login')
        }).catch(err => {
            authContext.resetUser()
        })
    }

    return (
        <>
            <ShowProfile profile={authContext.user}/>
            <Button className="mt-2" type='submit' size='lg' onClick={handleLogout}>
                Logout
            </Button>
        </>
    );
};


const ShowProfile = (props) => {
    const authContext = useContext(AuthContext);
    return (
        <>
            <div className='mt-4 d-flex flex-column justify-content-center align-items-center'>
                <Card style={{ width: '80vw' }}>
                <Card.Header className='text-center'>
                    <b>Profile's Information</b>
                    <Link className='ms-2' to="/editProfile" state={props.profile}>
                        <BsPencil/>
                    </Link>
                </Card.Header>
                <ListGroup variant="flush">
                <ListGroup.Item><b>Email: &nbsp;&nbsp; </b> {authContext.user.email}</ListGroup.Item>
                <ListGroup.Item><b>Date: &nbsp;&nbsp;</b> {authContext.user.dateOfBirth}</ListGroup.Item>
                <ListGroup.Item><b>Name: &nbsp;&nbsp;</b> {authContext.user.name}</ListGroup.Item>
                <ListGroup.Item><b>Surname: &nbsp;&nbsp;</b> {authContext.user.surname}</ListGroup.Item>
                <ListGroup.Item><b>Username: &nbsp;&nbsp;</b> {authContext.user.username}</ListGroup.Item>
                <ListGroup.Item><b>Role: &nbsp;&nbsp;</b> {authContext.user.role}</ListGroup.Item>
                </ListGroup>
                </Card>
            </div>
        </>
    );
}


export default ProfileContent;
