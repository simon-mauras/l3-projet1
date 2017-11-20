let n = 30 in
let m = 20 in
let max_x = 1000 in
let max_y = 500 in
let dx = 30 in
let dy = 30 in

print_int (n * m);
print_newline ();

for i=0 to n-1 do
  let x = Random.int max_x in
  let y = Random.int max_y in
  for j=0 to m-1 do
    print_int (x - dx + 2 * Random.int dx);
    print_string " ";
    print_int (y - dy + 2 * Random.int dy);
    print_newline ();
  done;
done;

