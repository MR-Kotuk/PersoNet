<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref} from "vue";

type Point = {
  x: number;
  y: number;
  vx: number;
  vy: number;
  color: string;
};

const canvasRef = ref<HTMLCanvasElement | null>(null);
let ctx: CanvasRenderingContext2D;
let animationId = 0;

const mouse = {x: 0, y: 0};

const COLORS: readonly string[] = ["#208af3", "#0a62b4"];
const POINTS_COUNT = 90;
const MAX_DISTANCE = 140;

let points: Point[] = [];

function resizeCanvas(): void {

  const canvas = canvasRef.value;

  if (!canvas) {
    return;
  }

  const oldWidth = canvas.width;
  const oldHeight = canvas.height;

  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;

  points.forEach(p => {
    p.x = (p.x / oldWidth) * canvas.width;
    p.y = (p.y / oldHeight) * canvas.height;
  });
}

function randomColor(): string {

  const index = Math.floor(Math.random() * COLORS.length);
  return COLORS[index]!;
}


function createPoints(): void {
  points = Array.from({length: POINTS_COUNT}, (): Point => ({
    x: Math.random() * window.innerWidth,
    y: Math.random() * window.innerHeight,
    vx: (Math.random() - 0.5) * 0.6,
    vy: (Math.random() - 0.5) * 0.6,
    color: randomColor()
  }));
}

function draw(): void {

  const canvas = canvasRef.value;

  if (!canvas) {
    return;
  }

  ctx.fillStyle = "black";
  ctx.fillRect(0, 0, canvas.width, canvas.height);

  for (const p of points) {
    p.x += p.vx;
    p.y += p.vy;

    if (p.x <= 0 || p.x >= canvas.width) p.vx *= -1;
    if (p.y <= 0 || p.y >= canvas.height) p.vy *= -1;

    ctx.beginPath();
    ctx.arc(p.x, p.y, 2, 0, Math.PI * 2);
    ctx.fillStyle = p.color;
    ctx.fill();
  }

  for (let i = 0; i < points.length; i++) {

    const p1 = points[i];

    if (!p1) {
      continue;
    }

    for (let j = i + 1; j < points.length; j++) {
      const p2 = points[j];

      if (!p2) {
        continue;
      }

      const dx = p1.x - p2.x;
      const dy = p1.y - p2.y;
      const dist = Math.hypot(dx, dy);

      if (dist < MAX_DISTANCE) {
        ctx.strokeStyle = `rgba(32,138,243,${1 - dist / MAX_DISTANCE})`;
        ctx.beginPath();
        ctx.moveTo(p1.x, p1.y);
        ctx.lineTo(p2.x, p2.y);
        ctx.stroke();
      }
    }

    const dx = p1.x - mouse.x;
    const dy = p1.y - mouse.y;
    const dist = Math.hypot(dx, dy);

    if (dist < MAX_DISTANCE) {
      ctx.strokeStyle = `rgba(10,98,180,${1 - dist / MAX_DISTANCE})`;
      ctx.beginPath();
      ctx.moveTo(p1.x, p1.y);
      ctx.lineTo(mouse.x, mouse.y);
      ctx.stroke();
    }
  }

  animationId = requestAnimationFrame(draw);
}

function onMouseMove(e: MouseEvent): void {

  mouse.x = e.clientX;
  mouse.y = e.clientY;
}

onMounted(() => {

  const canvas = canvasRef.value;
  if (!canvas) {
    return;
  }

  const context = canvas.getContext("2d");
  if (!context) {
    return;
  }

  ctx = context;

  resizeCanvas();
  createPoints();
  draw();

  window.addEventListener("resize", resizeCanvas);
  window.addEventListener("mousemove", onMouseMove);
});

onBeforeUnmount(() => {
  cancelAnimationFrame(animationId);
  window.removeEventListener("resize", resizeCanvas);
  window.removeEventListener("mousemove", onMouseMove);
});
</script>

<template>
  <canvas ref="canvasRef" class="network-bg"/>
</template>

<style scoped>
.network-bg {
  position: fixed;
  inset: 0;
  background: black;

  z-index: -1;
}
</style>
