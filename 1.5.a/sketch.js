// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Seeking "vehicle" follows the mouse position

// Implements Craig Reynold's autonomous steering behaviors
// One vehicle "seeks"
// See: http://www.red3d.com/cwr/

let v;
let d = 25;

function setup() {
  createCanvas(640, 360);
  v = new Vehicle(width / 2, height / 2);
  
}

function draw() {
  
  background(51);
  fill(127);
  stroke(200);
  strokeWeight(2);
  
  // Draw an ellipse at the mouse position
  let mouse = createVector(mouseX, mouseY);
  ellipse(mouse.x, mouse.y, 48, 48);
  

  // Call the appropriate steering behaviors for our agents
  
  v.boundaries();
  v.steering();
  v.update();
  v.display();
  
  v.seek(mouse);

}