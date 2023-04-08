
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
import {Container, Navbar as MyNavbar, Button, Dropdown, Row, Col, Form, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import {BsSearch, BsPencil, BsPlus} from "react-icons/bs";
import Api from "../../../services/Api";
import {useState} from "react";
import ProductTable from "../ProductTable/ProductTable";
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';

const ProfileContent = (props) => {

    const [profile, setProfile] = useState(null);
    const [search, setSearch] = useState(null);

    const handleSearch = (event) =>{
        event.preventDefault();
        Api.getProfileByEmail(search)
            .then(profile =>{
                console.log(profile)
                setProfile(profile);
            })
            .catch( err =>{
                console.log('errore'+ err);
            })
    }

    return (
        <>
                <Row className="w-75 justify-content-center">
                    <Col className="justify-content-start col-8">
                        <h1 className='fw-bold fst-italic mt-4'>
                            PROFILE
                        </h1>
                    </Col>
                    <Col className="justify-content-end mt-4">
                    <Form className="d-flex">
                        <Form.Control
                            type="search"
                            placeholder="Search"
                            className="me-2"
                            aria-label="Search"
                            data-testid="name-select"
                            onChange={(event) => { setSearch(event.target.value) }}

                        />
                        <Button type="submit" className='fw-bold fst-italic' onClick={event => handleSearch(event)}>
                            <BsSearch className='p-0 m-0'/>
                        </Button>
                    </Form>
                    </Col>
                </Row>

                {profile?
                    <ShowProfile profile={profile}/>
                    :
                    <p>Search for a profile</p>
                }
            <Link className='fw-bold fst-italic justify-content-end position-absolute end-0 bottom-0 m-5' to={"/newProfile"}>
                <Button>New Profile&nbsp;
                    <BsPlus className='p-0 m-0'></BsPlus>
                </Button>
            </Link>
        </>
    );
};


const ShowProfile = (props) => {
    return (
        <>
            <div className='mt-4 d-flex flex-column justify-content-center align-items-center'>
                <Card style={{ width: '20rem' }}>
                <Card.Header className='text-center'>
                    <b>Profile's Informations</b>
                    <Link className='ms-2' to="/editProfile" state={props.profile}>
                        <BsPencil/>
                    </Link>
                </Card.Header>
                <ListGroup variant="flush">
                <ListGroup.Item><b>Email: &nbsp;&nbsp; </b> {props.profile.email}</ListGroup.Item>
                <ListGroup.Item><b>Date: &nbsp;&nbsp;</b> {props.profile.dateOfBirth}</ListGroup.Item>
                <ListGroup.Item><b>Name: &nbsp;&nbsp;</b> {props.profile.name}</ListGroup.Item>
                <ListGroup.Item><b>Surname: &nbsp;&nbsp;</b> {props.profile.surname}</ListGroup.Item>
                <ListGroup.Item><b>Username: &nbsp;&nbsp;</b> {props.profile.username}</ListGroup.Item>
                </ListGroup>
                </Card>
            </div>
        </>
    );
}


export default ProfileContent;
