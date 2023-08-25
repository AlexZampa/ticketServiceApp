import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import Api from "../../../services/Api";
import { Badge, Row, Col } from "react-bootstrap";
import {useNavigate,Link} from "react-router-dom";


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
    },
	0: {
		name: "resolved",
		status: "dark"
	}
}

const DashboardCard = (props) => {
	const navigate = useNavigate();

	return (
		<Card style={{ width: "18rem", margin: "1rem" }}>
			<Card.Body>
				<Card.Title>
					<Row align="left">
						<Col>
							<b>Ticket {props.ticket.id}</b>
						</Col>

						<Col align="right">
							<Badge bg={"secondary"}>{props.ticket.status}</Badge>
						</Col>
					</Row>
				</Card.Title>
				<ListGroup variant="flush">
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ProductId</b>
							</Col>

							<Col align="right">{props.ticket.productId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>category</b>
							</Col>

							<Col align="right">{props.ticket.category}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>Priority</b>
							</Col>

							<Col align="right">
								{props.ticket.priority == 0 ? <b> - </b>:<Badge bg={priorityMap[props.ticket.priority].status}>{priorityMap[props.ticket.priority].name}</Badge>}

                                </Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ProfileId</b>
							</Col>

							<Col align="right">{props.ticket.profileId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ExpertId</b>
							</Col>

							<Col align="right">{props.ticket.expertId ? props.ticket.expertId:'not assigned'}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
					</ListGroup.Item>
				</ListGroup>
				<Card.Text>
					<b>Description: </b>
					{props.ticket.description}</Card.Text>
				<Link to={`/ticket/${props.ticket.id}`}>
					<Button variant="info" >More Options</Button>
				</Link>
				{props.ticket.expertId ?<Link to={`/ticket/${props.ticket.id}/chat`}>
					<Button variant="info" className="ms-2">Chat</Button>
				</Link>: <></>}

			</Card.Body>
		</Card>
	);
};

export default DashboardCard;
