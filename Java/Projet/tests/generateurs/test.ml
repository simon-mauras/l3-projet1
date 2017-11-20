let n = Scanf.scanf "%d\n" (fun x -> x) in
let m = Array.init n (fun _ -> Scanf.scanf "%d %d\n" (fun x y -> x, y)) in
for i = 0 to n-1 do
for j = i+1 to n-1 do
if m.(i) = m.(j) then begin
print_int i;
print_string " ";
print_int j;
print_newline();
end;
done;
done;
