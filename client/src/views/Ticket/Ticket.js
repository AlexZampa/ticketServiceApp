/*
 * --------------------------------------------------------------------
 *
 * Package:         client
 * Module:          src/views/Dashboard
 * File:            Home.jsx
 *
 * Copyright (c) 2022 - se2022-Team12
 * All rights reserved.
 * --------------------------------------------------------------------
 */

//Imports
import Api from "../../services/Api";
import { Row, Col } from "react-bootstrap";
import { useParams } from "react-router-dom";
import TicketCard from "../../components/ui-core/TicketComponent/TicketCard";


const ticket = {
	id: 1,
	productId: "A01",
	category: "assist",
	priority: 1,
	description: "problem description",
	profileId: 31,
	expertId: null,
	status: "OPEN",
};

const priorityMap ={
    1 : {
      name: "low",
      status: "success"
    },
    2: {
      name: "medium",
      status: "warning"
    },
    3: {
      name: "high",
      status: "danger"
    }
}

const Ticket = () => {
    const params = useParams();
	// useEffect(() => {
	//     Api.getAllProduct()
	//         .then(products =>{
	//             setProducts(products);
	//         })
	//         .catch( err =>{
	//             notify.error("Server error")
	//         })
	// }, []); //eslint-disable-line react-hooks/exhaustive-deps
	const ticketId = params.ticketId;
	return (
		<>
			<div className="mt-4 d-flex flex-column justify-content-center align-items-center">
				<Row className="w-75">
					<Col align="center">
						<h1 className="fw-bold fst-italic mt-4">TICKET PAGE</h1>
					</Col>
				</Row>
				<Row>
                    <TicketCard/>
				</Row>
			</div>
		</>
	);
};

export default Ticket;
