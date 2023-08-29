import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import { Badge, Row, Col, Form} from "react-bootstrap";
import {useContext, useEffect, useState} from "react";
import useNotification from "../../utils/useNotification";
import {AuthContext} from "../../utils/AuthContext";
import Api from "../../../services/Api";
import {useNavigate} from "react-router-dom";
import {ArrowLeftCircle} from "react-bootstrap-icons";
import ticket from "../../../views/Ticket/Ticket";
import Button from 'react-bootstrap/Button';


const priorityMap = {
    1: {
        name: "low",
        status: "success",
    },
    2: {
        name: "medium",
        status: "warning",
    },
    3: {
        name: "high",
        status: "danger",
    },
};

const TicketCreateComponent = (props) => {
    const authContext = useContext(AuthContext);
    const notify = useNotification()
    const navigate  = useNavigate();
    const [product,setProduct] = useState(null)
    const [description,setDescription] = useState(null)
    const [validationError, setValidationError] = useState(false);

    const handleProduct = (p) =>{
        setProduct(p.target.value.split(' - ')[0])
        setValidationError(false);
    }

    const handleSubmit = (event) =>{
        event.preventDefault();

        // Validate the form before proceeding
        if (!product) {
            setValidationError(true); // Show validation error if no value is selected
        } else {

        const filter = props.products.filter((prod) => prod.id == product)[0];
        Api.getProfileByEmail(authContext.user.email,authContext.user.token).then(
            user =>{
                const ticket = {
                    productId: filter.id,
                    category: filter.brand,
                    priority: 0,
                    description: description,
                    profileId: user.id,
                    expertId: null,
                    status: 'OPEN'

                }
                Api.addNewTicket(ticket, authContext.user.token).then(()=>{
                        notify.success('ticket created successfully')

                    }
                ).catch( err =>{
                    notify.error("Server error")
                })
            }
        ).catch( err =>{
            notify.error("Server error")
        })
        navigate('/dashboard')}
    }

    // props.products.filter((prod) => prod.id == p.target.value.split(' - ')[0])[0]

    return (
        <>
            <Form onSubmit={handleSubmit} >
                <Form.Group className="mb-3">
                    <Form.Label>ProductId</Form.Label>
                    <Form.Select onChange={handleProduct} isInvalid={validationError} >
                        <option>select a product</option>
                        {
                            props.products.map(product =>{
                                return(<option key={product.id}>{product.id} - {product.name}</option>)
                            })
                        }

                    </Form.Select>
                    <Form.Control.Feedback type="invalid">
                        Please select an option.
                    </Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Description</Form.Label>
                    <Form.Control placeholder="Insert description" onChange={(event) => {
                        setDescription(event.target.value)
                    }} required={true}/>
                    <Form.Text className="text-muted">
                        Insert a description to explain the problem
                    </Form.Text>
                </Form.Group>

                <Button variant="primary"  type={"submit"}>
                    Submit
                </Button>
            </Form>

        </>

    );
};

export default TicketCreateComponent;
