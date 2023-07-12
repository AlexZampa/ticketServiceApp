import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import Api from "../../../services/Api";
import { Badge, Row, Col } from "react-bootstrap";

const card = {
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

const DashboardCard = (props) => {
	return (
		<Card style={{ width: "18rem" }}>
			<Card.Body>
				<Card.Title>
					<Row align="left">
						<Col>
							<b>Ticket {card.id}</b>
						</Col>

						<Col align="right">
							<Badge bg={"success"}>Available</Badge>
						</Col>
					</Row>
				</Card.Title>
				<ListGroup variant="flush">
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ProductId</b>
							</Col>

							<Col align="right">{card.productId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>category</b>
							</Col>

							<Col align="right">{card.category}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>Priority</b>
							</Col>

							<Col align="right">
                                <Badge bg={priorityMap[card.priority].status}>{priorityMap[card.priority].name}</Badge>
                                </Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ProfileId</b>
							</Col>

							<Col align="right">{card.profileId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ExpertId</b>
							</Col>

							<Col align="right">{card.expertId}</Col>
						</Row>
					</ListGroup.Item>
				</ListGroup>
				<Card.Text>{card.description}</Card.Text>
				<Button variant="info">More Options</Button>
			</Card.Body>
		</Card>
	);
};

export default DashboardCard;
