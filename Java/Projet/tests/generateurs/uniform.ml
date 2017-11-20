let n = 300 in
let m = 600 in
print_int n;
print_newline();
for i=0 to n-1 do
  print_int ((Random.int m) - m/2);      
  print_string " ";
  print_int ((Random.int m) - m/2);
  print_newline ();
done;;
