import { Badge, Row, Col, Form} from "react-bootstrap";
import {useContext, useEffect, useState} from "react";
import useNotification from "../../utils/useNotification";
import {AuthContext} from "../../utils/AuthContext";
import Api from "../../../services/Api";
import {useNavigate} from "react-router-dom";
import Button from 'react-bootstrap/Button';
import {useParams} from "react-router-dom";


const ProductCreateComponent = (props) => {
    const authContext = useContext(AuthContext);
    const notify = useNotification()
    const param = useParams();
    const navigate  = useNavigate();
    const [id, setId] = useState(param.productId?param.productId:"")
    const [name, setName] = useState("")
    const [brand, setBrand] = useState("")
    const [description,setDescription] = useState("")
    const disable= param.productId? true: false;

    useEffect(() =>{
        if(param.productId){
            Api.getProduct(param.productId).then(
                product =>{
                    setBrand(product.brand);
                    setName(product.name);
                    setDescription(product.description)
                }
            ).catch(()=>{notify.err('Server Error')})
        }

    },[])


    const handleSubmit = () =>{
        const product = {
            id: id,
            name: name,
            brand: brand,
            description: description
        }
        if(param.productId){
            Api.modifyProduct(param.productId, product, authContext.user.token).then(()=> {

                notify.success('product modified successfully')
            }
            ).catch(()=>{notify.err('Server Error')})
        }
        else {
            Api.addNewProduct(product, authContext.user.token).then(()=>{
                    notify.success('product created successfully')
                }
            ).catch( err =>{
                notify.error("Server error")
            })
        }
        navigate('/')

    }

    return (
        <>
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>ID</Form.Label>
                    <Form.Control placeholder="Insert Identifier" value={id} required={true} disabled={disable} onChange={(event) => {
                        setId(event.target.value)
                    }} />
                    <Form.Text className="text-muted">
                        {param.productId?"":"Insert a correct ID"}
                    </Form.Text>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Name</Form.Label>
                    <Form.Control placeholder="Insert name" value={name} required={true} onChange={(event) => {
                        setName(event.target.value)
                    }} />
                    <Form.Text className="text-muted">
                        Insert Name of the product
                    </Form.Text>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Brand</Form.Label>
                    <Form.Control placeholder="Insert product brand" value={brand} required={true} onChange={(event) => {
                        setBrand(event.target.value)
                    }} />
                    <Form.Text className="text-muted">
                        Insert the Brand of the product
                    </Form.Text>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Description</Form.Label>
                    <Form.Control placeholder="Insert description" value={description} required={true} onChange={(event) => {
                        setDescription(event.target.value)
                    }} />
                    <Form.Text className="text-muted">
                        Insert a description of the product
                    </Form.Text>
                </Form.Group>

                <Button variant="primary"  type={"submit"}>
                    Submit
                </Button>
            </Form>

        </>

    );
};

export default ProductCreateComponent;
