
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
import {BsSearch} from "react-icons/bs";
import Api from "../../../services/Api";
import {useState} from "react";
import ProductTable from "../ProductTable/ProductTable";
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';

const ProfileContent = (props) => {

    const [profile, setProfile] = useState(null);
    const [search, setSearch] = useState(null);

    const handleSearch = () =>{
        Api.getProfileByEmail(search)
            .then(profile =>{
                setProfile(profile);
            })
            .catch( err =>{
                console.log('errore'+ err);
            })
    }

    return (
        <>
            <div>
                <Row className="w-75">
                    <Col className="justify-content-start col-8">
                        <h1 className='fw-bold fst-italic mt-4'>
                            PROFILE
                        </h1>
                    </Col>
                    <Col className="justify-content-end">
                        <Form className="mt-4">
                            <Form.Control
                                data-testid="name-select"
                                type="text"
                                placeholder="Search"
                                onChange={(event) => { setSearch(event.target.value) }}
                            />
                        </Form>
                    </Col>
                    <Col className="justify-content-end p-0">
                        <Button className=' fw-bold fst-italic mt-4' onClick={() => handleSearch()}>
                            <BsSearch className='p-0 m-0'/>
                        </Button>
                    </Col>
                </Row>

                {profile?
                    <ShowProfile profile={profile}/>
                    :
                    <p>Search for a profile</p>
                }
            </div>
        </>
    );
};


const ShowProfile = (props) => {
    return (
        <>
            <div className='mt-4 d-flex flex-column justify-content-center align-items-center'>
                <Card style={{ width: '18rem' }}>
                <Card.Header>Featured</Card.Header>
                <ListGroup variant="flush">
                <ListGroup.Item>Cras justo odio</ListGroup.Item>
                <ListGroup.Item>Dapibus ac facilisis in</ListGroup.Item>
                <ListGroup.Item>Vestibulum at eros</ListGroup.Item>
                </ListGroup>
                </Card>
            </div>
        </>
    );
}


export default ProfileContent;
