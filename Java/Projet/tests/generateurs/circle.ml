let n = 100 in
let r = 300. in
let pi = acos(-1.) in
print_int n;
print_newline();
for i=0 to n-1 do
  let angle = (float_of_int i) *. 2. *. pi /. (float_of_int n) in
  print_int (int_of_float (r *. cos angle));
  print_string " ";
  print_int (int_of_float (r *. sin angle));
  print_newline();
done;

